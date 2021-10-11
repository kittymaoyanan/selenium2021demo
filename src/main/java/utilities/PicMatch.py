#coding=utf-8
import os
import sys

import cv2

import numpy as np

def match_pt(template, img, temImg=None):


    if(temImg==None):
        temImg = img
    # img="F:\\project\\xiamen10086\\testproject5\\android_template\\img.png"
    # temImg = "F:\\project\\xiamen10086\\testproject5\\android_template\\temimg.png"
    # # temImg = "temimg2.png"
    # template = "F:\\project\\xiamen10086\\testproject5\\android_template\\template.png"

    try:
        img = cv2.imread(img)
        template = cv2.imread(template, 0)
        temImg = cv2.imread(temImg, 0)
        # 输入：template：模板(灰度图)  img：待匹配图(彩色图)  temImg: 模板的大图
        # MIN_MATCH_COUNT(选择输入,默认变量)：最小匹配数数量，能计算数量为4个，考虑误匹配点一般大于4个

        # 输出： dst = [列坐标位置, 行坐标位置]
        # 显示图片提取
        image = cv2.cvtColor(img, cv2.IMREAD_GRAYSCALE)
        # 创建sift检测器

        sift = cv2.xfeatures2d.SIFT_create()
        # 寻找关键点
        kp1, des1 = sift.detectAndCompute(template, None)
        kp2, des2 = sift.detectAndCompute(image, None)

        # 创建设置FLANN匹配
        FLANN_INDEX_KDTREE = 0
        index_params = dict(algorithm=FLANN_INDEX_KDTREE, trees=5)
        search_params = dict(checks=50)
        flann = cv2.FlannBasedMatcher(index_params, search_params)
        matches = flann.knnMatch(des1, des2, k=2)
        # 保存好的匹配点
        good = []
        # 舍弃大于0.7的匹配
        for m, n in matches:
            if m.distance < 0.6 * n.distance:
                good.append(m)
        src_pts = np.float32([kp1[m.queryIdx].pt for m in good]).reshape(-1, 1, 2)
        dst_pts = np.float32([kp2[m.trainIdx].pt for m in good]).reshape(-1, 1, 2)

        if len(good) >= 6:
            # 获取关键点的坐标
            src_pts = np.float32([kp1[m.queryIdx].pt for m in good]).reshape(-1, 1, 2)
            dst_pts = np.float32([kp2[m.trainIdx].pt for m in good]).reshape(-1, 1, 2)
            # 计算变换矩阵和MASK
            M, mask = cv2.findHomography(src_pts, dst_pts, cv2.RANSAC, 5.0)
            h, w = template.shape
            # 使用得到的变换矩阵对原图像的四个角进行变换，获得在目标图像上对应的坐标
            pts = np.float32([[0, 0], [0, h - 1], [w - 1, h - 1], [w - 1, 0]]).reshape(-1, 1, 2)
            dst = cv2.perspectiveTransform(pts, M)
            # dst = [int((dst[0][0][0] + dst[1][0][0] + dst[2][0][0] + dst[3][0][0]) / 4),
            #        int((dst[0][0][1] + dst[1][0][1] + dst[2][0][1] + dst[3][0][1]) / 4)]
            res = [max(dst[0][0][0], dst[1][0][0]), max(dst[0][0][1], dst[3][0][1]),
                   min(dst[2][0][0], dst[3][0][0]), min(dst[1][0][1], dst[2][0][1])]
            dia1 = np.sqrt(np.sum(np.square(dst[0][0] - dst[2][0])))
            dia2 = np.sqrt(np.sum(np.square(dst[1][0] - dst[3][0])))
            if min(dia1, dia2) / max(dia1, dia2) < 0.9:
                x0, y0 = np.mean(src_pts, 0)[0]
                x1, y1 = np.mean(dst_pts, 0)[0]
                w1, h1 = template.shape[1], template.shape[0]
                w2, h2 = temImg.shape[1], temImg.shape[0]
                w3, h3 = img.shape[1], img.shape[0]

                xmin = x1 - w3 * x0 / w2
                ymin = y1 - w3 * y0 / w2
                xmax = x1 - w3 * x0 / w2 + w1 * w3 / w2
                ymax = y1 - w3 * y0 / w2 + h1 * w3 / w2
                res = [xmin, ymin, xmax, ymax]

            # 显示尺度
            # scale = 2.5
            # img = cv2.resize(img, (int(img.shape[1] / scale), int(img.shape[0] / scale)))
            # dst = [int(res[0] / scale), int(res[1] / scale),
            #        int(res[2] / scale), int(res[3] / scale)]
            # cv2.rectangle(img, (dst[0], dst[1]), (dst[2], dst[3]), (0, 255, 0), 2)
            # cv2.imshow("img", img)
            # cv2.waitKey()
            # cv2.destroyAllWindows()
            # 输出[xmin, ymin, xmax, ymax, xcent, ycent]
            # self.clickEByXY(int((res[0] + res[2]) / 2), int((res[1] + res[3]) / 2))
            print(int((res[0] + res[2]) / 2))
            print(int((res[1] + res[3]) / 2))

            # return int((res[0] + res[2]) / 2), int((res[1] + res[3]) / 2)
        else:
            # if ((len(good) / len(kp1)) >= 0.05 and temImg != None):
            if ((len(good) / len(kp1)) >= 0.05):
                x0, y0 = np.mean(src_pts, 0)[0]
                x1, y1 = np.mean(dst_pts, 0)[0]
                w1, h1 = template.shape[1], template.shape[0]
                w2, h2 = temImg.shape[1], temImg.shape[0]
                w3, h3 = img.shape[1], img.shape[0]

                xmin = x1 - w3 * x0 / w2
                ymin = y1 - w3 * y0 / w2
                xmax = x1 - w3 * x0 / w2 + w1 * w3 / w2
                ymax = y1 - w3 * y0 / w2 + h1 * w3 / w2
                res = [xmin, ymin, xmax, ymax]

                ####显示尺度
                # scale = 2.5
                # img = cv2.resize(img, (int(img.shape[1] / scale), int(img.shape[0] / scale)))
                # dst = [int(res[0] / scale), int(res[1] / scale),
                #        int(res[2] / scale), int(res[3] / scale)]
                # cv2.rectangle(img, (dst[0], dst[1]), (dst[2], dst[3]), (0, 255, 0), 2)
                # cv2.imshow("img", img)
                # cv2.waitKey()
                # cv2.destroyAllWindows()
                # 输出[xmin, ymin, xmax, ymax, xcent, ycent]
                # self.clickEByXY(int((res[0] + res[2]) / 2), int((res[1] + res[3]) / 2))
                print(int((res[0] + res[2]) / 2))
                print(int((res[1] + res[3]) / 2))
                # return int((res[0] + res[2]) / 2), int((res[1] + res[3]) / 2)
            else:
                print("error cannot find element", len(good), len(good) / len(kp1), len(good) / len(kp2))
    except Exception as e:
        print("error:"+e)
            # return None
if __name__ == '__main__':
    match_pt(sys.argv[1],sys.argv[2],sys.argv[3])