
package com.makcsv.css;

import com.makcsv.helpers.FileHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Max Soloviov <makcsv@bigmir.net>
 */
public class CssParser {
    
    private CssParser() {}
    
    public static ParsedCss parse(String cssText) throws Exception {
        
        if (cssText.isEmpty()) {
            throw new Exception("CssParser: an empty css string given!");
        }
        
        return new ParsedCss(_parse(cssText));
        
    }
    
    public static ParsedCss parse(File cssFile) throws Exception {
        
        if (!cssFile.exists()) {
            throw new Exception("CssParser: css file not found!");
        }
        
        String cssText = FileHelper.getFileContentAsString(cssFile);
        
        return new ParsedCss(_parse(cssText));
        
    }
    
    private static List<CssRule> _parse(String cssText) {
        
        List<CssRule> rules = _getRulesList(cssText, "", 0);
        
        return rules;
        
    }
    
    private static List<CssRule> _getRulesList(String cssText, String parentRuleSelector, int nestingLvl) {
        
        List<CssRule> rules = new ArrayList<>();
        
        Stack<Character> stack = new Stack<>();
        StringBuilder sb = new StringBuilder();
        
        boolean write = false, isAtRule = false, comment = false;
        
        for (int i = 0; i < cssText.length(); i++) {
            
            char c = cssText.charAt(i), 
                cn = (i < cssText.length() - 1) ? cssText.charAt(i + 1) : '\0';
            
            if ('/' == c && '*' == cn) {
                comment = true;
            }
            
            if ('*' == c && '/' == cn) {
                comment = false;
                i++;
                continue;
            }
            
            if (comment) {
                continue;
            }
            
            if (stack.empty() && !Character.isWhitespace(c)) {
                
                write = true;
                
                // Отмечаем @page, @viewport и @font-face как обычные правила, 
                // по скольку они имеют такую же структуру
                if ('@' == c && ('p' == cn || 'f' == cn || 'v' == cn)) {
                    isAtRule = false;
                } else if ('@' == c) {
                    isAtRule = true;
                }
                
            }
            
            if (write) {
                sb.append(c);
            }
            
            if ('{' == c) {
                stack.add(c);
            }
            
            if (write && ';' == c && stack.empty()) {
                
                rules.add(new CssRule(sb.toString(), isAtRule, parentRuleSelector, nestingLvl));
                
                sb.setLength(0);
                write = false;
                isAtRule = false;
                
                continue;
                
            }
            
            if ('}' == c && !stack.empty() && '{' == stack.peek()) {
                
                stack.pop();
                
                if (stack.empty()) {
                    
                    String ruleTxt = sb.toString();
                    List<CssRule> subRules = null;
                    
                    if (isAtRule && ruleTxt.contains("{") && ruleTxt.contains("}")) {
                        
                        String parentSelector = ruleTxt.substring(0, ruleTxt.indexOf("{")).trim();
                        
                        subRules = _getRulesList(ruleTxt.substring(ruleTxt.indexOf("{") + 1, ruleTxt.lastIndexOf("}")), parentSelector, nestingLvl + 1);
                        
                    }
                    
                    if (null != subRules) {
                        
                        rules.addAll(subRules);
                        
                    } else {
                        
                        rules.add(new CssRule(ruleTxt, isAtRule, parentRuleSelector, nestingLvl));
                        
                    }
                    
                    sb.setLength(0);
                    write = false;
                    isAtRule = false;
                    
                }
                
            }
            
        }
        
        return rules;
        
    }
    
}