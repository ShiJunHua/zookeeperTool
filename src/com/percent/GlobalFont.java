package com.percent;

import java.awt.*;

/**
 * @Description:
 * @Auther:史俊华
 * @Date:2018/12/716
 */
public class GlobalFont {
    private static Font font = new Font("alias", Font.PLAIN, 14);

    public static Font getFont() {
        return font;
    }

    public static void setFont(Font font) {
        GlobalFont.font = font;
    }
}
