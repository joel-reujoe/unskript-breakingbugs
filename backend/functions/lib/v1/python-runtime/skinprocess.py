# import the necessary packages
#from pyimagesearch import imutils
import numpy as np
import argparse
import cv2
import sys
from matplotlib import pyplot as plt
 
# define the upper and lower boundaries of the HSV pixel
# intensities to be considered 'skin'
lower = np.array([0, 0, 150], dtype = "uint8")
upper = np.array([150, 252, 252], dtype = "uint8")

imgg = cv2.imread(sys.argv[1])
img = imgg.copy()
img[:, :, 0] = imgg[:, :, 2]
img[:, :, 2] = imgg[:, :, 0]


#frame = imutils.resize(frame, width = 400)
converted = cv2.cvtColor(img, cv2.COLOR_RGB2HSV)
skinMask = cv2.inRange(converted, lower, upper)


 
# apply a series of erosions and dilations to the mask
# using an elliptical kernel
kernel = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (11, 11))
skinMask = cv2.erode(skinMask, kernel, iterations = 2)
skinMask = cv2.dilate(skinMask, kernel, iterations = 2)
 
# blur the mask to help remove noise, then apply the
# mask to the frame
skinMask = cv2.GaussianBlur(skinMask, (3, 3), 0)
skin = cv2.bitwise_and(imgg, imgg, mask = skinMask)
imgy = skin.copy()
imgy[:, :, 0] = skin[:, :, 2]
imgy[:, :, 2] = skin[:, :, 0]


cv2.imwrite("image.jpg", imgy)

image = cv2.imread("image.jpg", 0)
a = cv2.countNonZero(image)
b = image.size
c = (a * 100)/b

if  0 <= c < 30:
    print ("Is a not skin")
    sys.stdout.flush()
else:
    print ("Is a Skin")
    sys.stdout.flush()
