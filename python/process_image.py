import argparse
import os

import cv2
import numpy as np
from PIL import Image
from PIL import ImageOps

THRESHOLD = 0.6
SIZE = 640

# initialize the list of class labels MobileNet SSD was trained to
# detect, then generate a set of bounding box colors for each class
CLASSES = ["background", "aeroplane", "bicycle", "bird", "boat",
           "bottle", "bus", "car", "cat", "chair", "cow", "diningtable",
           "dog", "horse", "motorbike", "person", "pottedplant", "sheep",
           "sofa", "train", "tvmonitor"]
COLORS = np.random.uniform(0, 255, size=(len(CLASSES), 3))


class Rect():
    def __init__(self, start_x=0, start_y=0, end_x=0, end_y=0):
        self.start_x = start_x
        self.start_y = start_y
        self.end_x = end_x
        self.end_y = end_y


def get_img_detections_and_dims(file):
    print("get_img_detections_and_dims() started...")

    dir_name = os.path.dirname(__file__)
    prototxt_file = os.path.join(dir_name, "MobileNetSSD_deploy.prototxt")
    caffemodel_file = os.path.join(dir_name, "MobileNetSSD_deploy.caffemodel")

    net = cv2.dnn.readNetFromCaffe(prototxt_file, caffemodel_file)
    image = cv2.imread(file)
    (h, w) = image.shape[:2]
    blob = cv2.dnn.blobFromImage(cv2.resize(image, (300, 300)), 0.007843, (300, 300), 127.5)
    # pass the blob through the network and obtain the detections and predictions
    net.setInput(blob)
    return net.forward(), (h, w)


def get_detections_bounding_boxes(detections, img_height, img_width):
    print("get_detections_bounding_boxes() started...")

    boxes = []
    for i in np.arange(0, detections.shape[2]):
        confidence = detections[0, 0, i, 2]
        # filter out weak detections by ensuring the `confidence` is greater than the minimum confidence
        if confidence > THRESHOLD:
            # idx = int(detections[0, 0, i, 1])
            box = detections[0, 0, i, 3:7] * np.array([img_width, img_height, img_width, img_height])
            (start_x, start_y, end_x, end_y) = box.astype("int")
            boxes.append(Rect(start_x, start_y, end_x, end_y))
    return boxes


def union(a, b):
    start_x = min(a.start_x, b.start_x)
    start_y = min(a.start_y, b.start_y)
    end_x = max(a.end_x, b.end_x)
    end_y = max(a.end_y, b.end_y)
    return Rect(start_x, start_y, end_x, end_y)


def get_union_bounding_box_center(bounding_boxes, img_height, img_width):
    print("get_union_bounding_box_center() started...")

    union_rect = Rect()
    for i in range(len(bounding_boxes)):
        if i is 0:
            union_rect = bounding_boxes[i]
        else:
            union_rect = union(union_rect, bounding_boxes[i])

    return (union_rect.start_x + union_rect.end_x) / 2 / img_width, \
           (union_rect.start_y + union_rect.end_y) / 2 / img_height


def crop_and_save_img(file, crop_center):
    print("crop_and_save_img() started...")

    im = Image.open(file)
    fit = ImageOps.fit(im, (SIZE, SIZE), centering=crop_center)
    dir_name = os.path.dirname(__file__)
    file_name = os.path.join(dir_name, '../croppedimages/', 'processed.jpg')
    fit.save(file_name)


def process_image(file):
    print("process_image() started...")

    dir_name = os.path.dirname(__file__)
    file = os.path.join(dir_name, '../croppedimages/', file)

    detections, (img_height, img_width) = get_img_detections_and_dims(file)

    bounding_boxes = get_detections_bounding_boxes(detections, img_height, img_width)

    crop_center = (0.5, 0.5)
    if bounding_boxes:  # some objects found
        crop_center = get_union_bounding_box_center(bounding_boxes, img_height, img_width)

    crop_and_save_img(file, crop_center)


if __name__ == '__main__':
    # construct the argument parse and parse the arguments
    ap = argparse.ArgumentParser()
    ap.add_argument("-i", "--image", required=True, help="file name (in /croppedimages folder")
    args = vars(ap.parse_args())
    process_image(args["image"])
