import numpy as np
import cv2
import argparse
from PIL import Image
from PIL import ImageOps

THRESHOLD = 0.2
SIZE = 640

# initialize the list of class labels MobileNet SSD was trained to
# detect, then generate a set of bounding box colors for each class
CLASSES = ["background", "aeroplane", "bicycle", "bird", "boat",
           "bottle", "bus", "car", "cat", "chair", "cow", "diningtable",
           "dog", "horse", "motorbike", "person", "pottedplant", "sheep",
           "sofa", "train", "tvmonitor"]
COLORS = np.random.uniform(0, 255, size=(len(CLASSES), 3))

class Box(object):
    def __init__(self, rect, prob, label):
        self._rect = rect
        self._prob = prob
        self._label = label

    @property
    def rect(self):
        return self._rect

    @rect.setter
    def rect(self, value):
        self._rect = value

    @property
    def prob(self):
        return self._prob

    @prob.setter
    def prob(self, value):
        self._prob = value

    @property
    def label(self):
        return self._label

    @label.setter
    def label(self, value):
        self._label = value


class Rect():
    def __init__(self, start_x=0, start_y=0, end_x=0, end_y=0):
        self.start_x = start_x
        self.start_y = start_y
        self.end_x = end_x
        self.end_y = end_y


def union(a, b):
    start_x = min(a[0], b[0])
    start_y = min(a[1], b[1])
    end_x = max(a[2], b[2])
    end_y = max(a[3], b[3])
    return start_x, start_y, end_x, end_y


def process_image(file = "Fashion.jpg"):
    print("Process image start")
    net = cv2.dnn.readNetFromCaffe("python/MobileNetSSD_deploy.prototxt", "python/MobileNetSSD_deploy.caffemodel")
    image = cv2.imread(file)
    (h, w) = image.shape[:2]
    blob = cv2.dnn.blobFromImage(cv2.resize(image, (300, 300)), 0.007843, (300, 300), 127.5)
    # pass the blob through the network and obtain the detections and predictions
    net.setInput(blob)
    detections = net.forward()

    boxes = []
    for i in np.arange(0, detections.shape[2]):
        # extract the confidence (i.e., probability) associated with the prediction
        confidence = detections[0, 0, i, 2]

        # filter out weak detections by ensuring the `confidence` is greater than the minimum confidence
        if confidence > THRESHOLD:
            # extract the index of the class label from the `detections`,
            # then compute the (x, y)-coordinates of the bounding box for the object
            idx = int(detections[0, 0, i, 1])
            box = detections[0, 0, i, 3:7] * np.array([w, h, w, h])
            (startX, startY, endX, endY) = box.astype("int")

            boxes.append(Box((startX, startY, endX, endY), confidence, CLASSES[idx]))

    print("some objects found")
    union_rect = None
    for i in range(len(boxes)):
        if i is 0:
            union_rect = boxes[i].rect
        else:
            union_rect = union(union_rect, boxes[i].rect)

    # expand union_rect
    union_rect = (max(0, union_rect[0] - 10), max(0, union_rect[1] - 10), min(w, union_rect[2] + 10),
                  min(h, union_rect[3] + 10))

    rect_center = (union_rect[0] + union_rect[2]) / 2 / w, (union_rect[1] + union_rect[3]) / 2 / h

    im = Image.open(file)
    fit = ImageOps.fit(im, (SIZE, SIZE), centering=rect_center)
    print("preparing to send cropped image")
    fit.save("croppedimages/processed.jpg")

if __name__ == '__main__':
    # construct the argument parse and parse the arguments
    ap = argparse.ArgumentParser()
    ap.add_argument("-i", "--image", required=True, help="path to input image")
    args = vars(ap.parse_args())
    process_image(args["image"])