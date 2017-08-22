package me.markoutte.sandbox.competitions.codewars;

/**
 * Jaden Smith, the son of Will Smith, is the star of films such as The Karate Kid (2010) and After Earth (2013). Jaden is also known for some of his philosophy that he delivers via Twitter. When writing on Twitter, he is known for almost always capitalizing every word.
 *
 *  Your task is to convert strings to how they would be written by Jaden Smith. The strings are actual quotes from Jaden Smith, but they are not capitalized in the same way he originally typed them.
 *
 *  Example:
 *  <code>
 *      Not Jaden-Cased: "How can mirrors be real if our eyes aren't real"
 *      Jaden-Cased:     "How Can Mirrors Be Real If Our Eyes Aren't Real"
 *  </code>
 * Note that the Java version expects a return value of null for an empty string or null.
 */
public class JadenCase {

    public String toJadenCase(String phrase) {
        if (phrase == null || phrase.trim().isEmpty()) {
            return null;
        }

        String[] parts = phrase.split(" ");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (i > 0) {
                result.append(" ");
            }
            result.append(part.substring(0, 1).toUpperCase()).append(part.substring(1));
        }

        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(new JadenCase().toJadenCase("How can mirrors be real if our eyes aren't real"));
    }

}
