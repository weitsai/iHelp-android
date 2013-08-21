package edu.stu.tool;

public class PointInPolygon {
    static double kaohsiungCoordinatesArr[][] = {
            {
                    120.33110316211459, 22.755069659494975
            }, {
                    120.34042434461566, 22.730225204295984
            }, {
                    120.31486348961717, 22.69592817524841
            }, {
                    120.35000688787372, 22.63878316498237
            }, {
                    120.32469971216655, 22.58613149637791
            }, {
                    120.37053573168821, 22.587099168501503
            }, {
                    120.39875523556837, 22.54889004546994
            }, {
                    120.35925858451884, 22.506907668640668
            }, {
                    120.33469417367293, 22.522175210686594
            }, {
                    120.26576360015031, 22.610599118899355
            }, {
                    120.25149104207544, 22.64527533081007
            }, {
                    120.27755446432343, 22.737929619261056
            }, {
                    120.3257351525257, 22.735977780201864
            }, {
                    120.33110316211459, 22.755069659494975
            }
    };

    public void getCity(double y, double x) {

        int right_node = 0, left_node = 0;
        double lasty = 0.0, lastx = 0.0;

        for (int i = 0; i < kaohsiungCoordinatesArr.length; i++) {
            if (lastx == 0.0) {
                lastx = kaohsiungCoordinatesArr[i][0];
                lasty = kaohsiungCoordinatesArr[i][1];
                continue;
            }

            if ((kaohsiungCoordinatesArr[i][1] >= y && y >= lasty) || (lasty >= y && y >= kaohsiungCoordinatesArr[i][1])) {
                if (x >= kaohsiungCoordinatesArr[i][0] && x >= lastx) {
                    right_node++;
                } else if (x <= kaohsiungCoordinatesArr[i][0] && x <= lastx) {
                    left_node++;
                } else {
                    if (x >= (kaohsiungCoordinatesArr[i][0] - lastx)) {
                        right_node++;
                    } else {
                        left_node++;
                    }
                }
            }

            lastx = kaohsiungCoordinatesArr[i][0];
            lasty = kaohsiungCoordinatesArr[i][1];
        }

        if (left_node % 2 == 1 && right_node % 2 == 1) {
            System.out.println("高雄市");
        } else {
            System.out.println("你不在高雄市內喔！");
        }

    }
}
