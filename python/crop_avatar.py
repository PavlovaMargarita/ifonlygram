import argparse
import os
import shutil

import cv2
from PIL import Image

TROLL_FACE = 'troll_face.jpg'

AVATAR_DIR = '../avatar/'

CROPPED_JPG = 'cropped.jpg'


def get_face_rect(img_path):
    dir_name = os.path.dirname(__file__)
    haarcascade_face_file = os.path.join(dir_name, "haarcascade_frontalface_default.xml")
    face_cascade = cv2.CascadeClassifier(haarcascade_face_file)

    img = cv2.imread(img_path)
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

    img_height, img_width = img.shape[:2]

    faces = face_cascade.detectMultiScale(gray, 1.3, 5)
    if len(faces):  # found
        (x, y, w, h) = faces[0]
        indent_w = w // 2
        indent_h = h // 2
        res = (max(0, x - indent_w), max(0, y - indent_w),
               min(img_width, x + w + indent_w), min(img_height, y + h + indent_h))
    else:
        res = None

    return res


def crop_face(file):
    dir_name = os.path.dirname(__file__)
    img_path = os.path.join(dir_name, "../avatar/", file)
    face_rect = get_face_rect(img_path)

    if face_rect:
        im = Image.open(img_path)
        crop = im.crop(face_rect)
        out_file_name = os.path.join(dir_name, AVATAR_DIR, CROPPED_JPG)
        crop.save(out_file_name)
    else:
        avatar_dir = os.path.join(dir_name, AVATAR_DIR)
        src_file = os.path.join(dir_name, TROLL_FACE)
        shutil.copy(src_file, avatar_dir)

        dst_file = os.path.join(avatar_dir, TROLL_FACE)
        new_dst_file_name = os.path.join(avatar_dir, CROPPED_JPG)
        os.rename(dst_file, new_dst_file_name)


if __name__ == '__main__':
    ap = argparse.ArgumentParser()
    ap.add_argument("-i", "--image", required=True, help="file name (in /avatar folder")
    args = vars(ap.parse_args())
    crop_face(args["image"])
