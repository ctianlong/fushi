package springboot.util;

import java.util.Random;

/**
 * 
 * @author Tianlong
 *
 */
public class RandomUtil
{
    private static final Random RANDOM = new Random();
    /**
     * 产生 min - max（包含边界）的随机数
     * @param min 小数
     * @param max 比min大的数
     * @return int 随机数字
     */
    public static int num(int min, int max)
    {
        return min + RANDOM.nextInt(max - min + 1);
    }

    /**
     * 产生 0 - num（包含边界） 的随机数
     * @param num 数字
     * @return int 随机数字
     */
    public static int num(int num)
    {
        return RANDOM.nextInt(num + 1);
    }

}