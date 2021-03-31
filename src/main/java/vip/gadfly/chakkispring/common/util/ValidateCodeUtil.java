package vip.gadfly.chakkispring.common.util;

import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

/**
 * @author Gadfly
 */

@SuppressWarnings("SpellCheckingInspection")
public class ValidateCodeUtil {

    public static final String sessionKey = "JCCODE";
    private static final Random random = new Random();
    private static final int width = 165; // 验证码的宽
    private static final int height = 45; // 验证码的高
    private static final int lineSize = 30; // 验证码中夹杂的干扰线数量
    private static final int randomStrNum = 4; // 验证码字符个数
    private static final String randomString = "23456789abcdefghijkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWSYZ";

    /**
     * 颜色的设置
     */
    private static Color getRandomColor(int fc, int bc) {

        fc = Math.min(fc, 255);
        bc = Math.min(bc, 255);

        int r = fc + random.nextInt(bc - fc - 16);
        int g = fc + random.nextInt(bc - fc - 14);
        int b = fc + random.nextInt(bc - fc - 12);

        return new Color(r, g, b);
    }

    /**
     * 字体的设置
     */
    private static Font getFont() throws IOException, FontFormatException {
        ClassPathResource dejavuSerifBold = new ClassPathResource("DejaVuSerif-Bold.ttf");
        return Font.createFont(Font.TRUETYPE_FONT, dejavuSerifBold.getInputStream()).deriveFont(Font.BOLD, 36);
    }

    /**
     * 干扰线的绘制
     */
    private void drawLine(Graphics2D g) {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int xl = random.nextInt(20);
        int yl = random.nextInt(10);
        g.setStroke(new BasicStroke(2.0f));
        g.setColor(getRandomColor(68, 190));
        g.drawLine(x, y, x + xl, y + yl);
    }

    /**
     * 随机字符的获取
     */
    private String getRandomString(int num) {
        num = num > 0 ? num : randomString.length();
        return String.valueOf(randomString.charAt(random.nextInt(num)));
    }

    /**
     * 字符串的绘制
     */
    private String drawString(Graphics2D g, String randomStr, int i) throws IOException, FontFormatException {
        g.setFont(getFont());
        g.setColor(getRandomColor(28, 200));
        String rand = getRandomString(random.nextInt(randomString.length()));
        randomStr += rand;
        // 设置每个字符的随机旋转
        double radianPercent = (random.nextBoolean() ? -1 : 1) * Math.PI * (random.nextInt(60) / 280D);
        g.rotate(radianPercent, 38 * i + 10, 35);
        g.translate(random.nextInt(3), random.nextInt(6));
        g.drawString(rand, 38 * i + 10, 25);
        g.rotate(-radianPercent, 38 * i + 10, 35);
        return randomStr;
    }

    private BufferedImage getBufferedImage(HttpSession session) throws IOException, FontFormatException {
        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.fillRect(0, 0, width, height);
        g.setColor(getRandomColor(105, 189));
        g.setFont(getFont());
        // 干扰线
        for (int i = 0; i < lineSize; i++) {
            drawLine(g);
        }
        // 随机字符
        String randomStr = "";
        for (int i = 0; i < randomStrNum; i++) {
            randomStr = drawString(g, randomStr, i);
        }
        g.dispose();
        // 移除之前的session中的验证码信息
        session.removeAttribute(sessionKey);
        // 重新将验证码放入session
        session.setAttribute(sessionKey, randomStr);
        session.setMaxInactiveInterval(300);
        return image;
    }

    /**
     * 生成随机图片的base64编码字符串
     *
     * @param session session
     * @return base64
     */
    public String getRandomCodeBase64(HttpSession session) throws IOException, FontFormatException {
        BufferedImage image = getBufferedImage(session);
        // 返回 base64
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", bos);

        byte[] bytes = bos.toByteArray();
        Base64.Encoder encoder = Base64.getEncoder();

        return encoder.encodeToString(bytes);
    }
}