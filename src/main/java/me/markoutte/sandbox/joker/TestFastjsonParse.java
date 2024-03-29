package me.markoutte.sandbox.joker;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;

import java.nio.charset.Charset;

public class TestFastjsonParse {

    public static void main(String[] args) {
        try {
            byte[] s = new byte[]{123, 34, 110, 97, 109, 101, 34, 58, 32, 123, 119, 115, 52, 66, 53, 64, 66, 32, 91, 49, 44, 32, 50, 51, 47, 53, 56, 62, 51, 49, 47, 47, 65, 58, 64, 63, 66, 58, 51, 61, 58, 60, 55, 53, 54, 52, 63, 66, 48, 52, 50, 50, 49, 57, 64, 50, 50, 59, 49, 52, 9, 45, 20, 18, 38, 25, 41, 19, 38, 31, 46, 39, 38, 23, 17, 8, 31, 26, 17, 43, 30, 28, 34, 37, 22, 17, 43, 21, 44, 38, 8, 13, 13, 35, 24, 33, 32, 15, 12, 12, 9, 28, 19, 10, 45, 27, 39, 13, 39, 9, 34, 25, 29, 35, 29, 25, 15, 45, 12, 41, 40, 39, 19, 37, 16, 45, 10, 44, 17, 29, 23, 32, 14, 16, 19, 42, 30, 9, 18, 28, 21, 11, 30, 24, 41, 33, 11, 20, 32, 20, 37, 14, 41, 33, 36, 32, 21, 41, 46, 36, 26, 36, 13, 25, 42, 20, 46, 37, 12, 16, 15, 33, 9, 28, 16, 19, 37, 16, 23, 11, 22, 27, 45, 9, 8, 39, 10, 27, 26, 21, 12, 20, 27, 43, 17, 26, 34, 12, 15, 25, 45, 15, 33, 22, 17, 25, 44, 17, 18, 18, 14, 8, 17, 39, 120, -31, 25, 32, 40, 47, 38, 0, 95, 72, -39, 107, 115, 66, -28, 109, 90, 24, -14, -50, -15, -22, 49, 122, 81, -1, -44, 13, 18, -29, 99, -33, 30, 115, 43, 58, -34, -32, 79, 69, -2, -45, -13, 78, 47, 67, 81, 32, 32, 25, 105, 43, 61, -45, 28, -41};
            System.out.println(JSON.parse(new String(s, Charset.forName("koi8"))));
        } catch (JSONException e) {
            System.out.println("This is fine");
        }
    }

}
