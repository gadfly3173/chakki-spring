package vip.gadfly.chakkispring.common.validator;

import lombok.extern.slf4j.Slf4j;
import vip.gadfly.chakkispring.common.annotation.MultimediaWordCount;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class MultimediaWordCountValidator implements ConstraintValidator<MultimediaWordCount, String> {

    public int maxMultimediaWordCount;
    public int minMultimediaWordCount;

    public void initialize(MultimediaWordCount multimediaWordCount) {
        this.maxMultimediaWordCount = multimediaWordCount.max();
        this.minMultimediaWordCount = multimediaWordCount.min();
    }

    @Override
    public boolean isValid(String StringValue, ConstraintValidatorContext context) {

        if (StringValue == null) {
            return true;
        }
        int f = countWords(delHtmlTags(StringValue));
        log.debug("字数为：" + f);
        return f >= minMultimediaWordCount && f <= maxMultimediaWordCount;
    }

    private String delHtmlTags(String htmlStr) {
        htmlStr = htmlStr.toLowerCase();
        //定义script的正则表达式
        String scriptRegex="<script[^>]*?>[\\s\\S]*?</script>";
        //定义style的正则表达式，去除style样式，防止css代码过多时只截取到css样式代码
        String styleRegex="<style[^>]*?>[\\s\\S]*?</style>";
        //定义HTML标签的正则表达式，去除标签，只提取文字内容
        String htmlRegex="<[^>]+>";

        // 过滤script标签
        htmlStr = htmlStr.replaceAll(scriptRegex, "");
        // 过滤style标签
        htmlStr = htmlStr.replaceAll(styleRegex, "");
        // 过滤html标签
        htmlStr = htmlStr.replaceAll(htmlRegex, "");
        return htmlStr; // 返回文本字符串
    }

    private int countWords(String s){

        int wordCount = 0;

        boolean word = false;
        int endOfLine = s.length() - 1;

        for (int i = 0; i < s.length(); i++) {
            // if the char is a letter, word = true.
            if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
                word = true;
                // if char isn't a letter and there have been letters before,
                // counter goes up.
            } else if (!Character.isLetter(s.charAt(i)) && word) {
                wordCount++;
                word = false;
                // last word of String; if it doesn't end with a non letter, it
                // wouldn't count without this.
            } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }
}
