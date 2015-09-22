
package com.makcsv.css;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Max Soloviov <makcsv@bigmir.net>
 */
public class ParsedCss {
    
    private List<CssRule> _cssRules;
    
    public ParsedCss(List<CssRule> rulesList) {
        
        this._cssRules = rulesList;
        
    }
    
    public List<CssRule> getRulesList() {
        
        return this._cssRules;
        
    }
    
    public String asText() {
        
        StringBuilder sb = new StringBuilder();
        String prevLvlParentSelector = "", prevParentSelector = "";
        int prevElNestingLvl = 0;
        
        for (CssRule rule : this._cssRules) {
            
            int parentRuleTabs = (rule.getNestingLvl() < 2) ? 0 : rule.getNestingLvl() - 1,
                selectorTabs = (!rule.getParentRuleSelector().isEmpty()) ? parentRuleTabs + 1 : parentRuleTabs,
                propertiesTabs = selectorTabs + 1;
            
            if (rule.getNestingLvl() > prevElNestingLvl) {
                prevLvlParentSelector = prevParentSelector;
            }
            
            // Если у правила есть родительское, и оно не такое же как у предыдущего -
            // открываем новый родительский блок
            if (!rule.getParentRuleSelector().isEmpty() && !rule.getParentRuleSelector().equalsIgnoreCase(prevParentSelector)) {
                
                if (!prevParentSelector.isEmpty()) {
                    
                    sb.append(_getLvlTabulation(parentRuleTabs));
                    sb.append("\n}\n");
                    
                }
                
                sb.append(_getLvlTabulation(parentRuleTabs));
                sb.append(rule.getParentRuleSelector());
                sb.append(" {\n");
                
            }
            
            // Если у текущего елемента уровень вложенности меньше чем у пред. - 
            // закрываем пред. родительский блок
            if (rule.getNestingLvl() < prevElNestingLvl) {
                
                sb.append(_getLvlTabulation(selectorTabs));
                sb.append("}\n");
                
                if (!prevLvlParentSelector.equalsIgnoreCase(rule.getParentRuleSelector())) {
                    sb.append("}\n");
                }
                
                // Если у елем. есть родительский блок - открываем его
                if (!rule.getParentRuleSelector().isEmpty()) {
                    
                    sb.append(_getLvlTabulation(parentRuleTabs));
                    sb.append(rule.getParentRuleSelector());
                    sb.append(" {\n");
                    
                }
                
            }
            
            // Вывод селектора
            sb.append(_getLvlTabulation(selectorTabs));
            sb.append(rule.getSelector());
            
            // Открытие блока свойств правила, если необходимо
            if (!rule.isAtRule() || (rule.isAtRule() && rule.hasChildren())) {
                sb.append(" {");
            }
            
            for (CssProperty property : rule.getPropertiesList()) {
                
                if (!property.getName().isEmpty()) {
                    
                    sb.append("\n");
                    sb.append(_getLvlTabulation(propertiesTabs));
                    sb.append(property.getName());
                    sb.append(": ");
                    
                } else {
                    
                    sb.append(" ");
                    
                }
                
                sb.append(property.getValue());
                sb.append(";");
                
            }
            
            if (!rule.isAtRule() || (rule.isAtRule() && rule.hasChildren())) {
                
                sb.append("\n");
                sb.append(_getLvlTabulation(selectorTabs));
                sb.append("}");
                
            }
            
            sb.append("\n");
            
            prevParentSelector = rule.getParentRuleSelector();
            prevElNestingLvl = rule.getNestingLvl();
            
        }
        
        if (prevElNestingLvl > 0) {
            
            for (int i = 0; i < prevElNestingLvl; i++) {
                sb.append("}\n");
            }
            
        }
        
        return sb.toString();
        
    }
    
    public String asMinifiedText() {
        
        String css = this.asText();
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < css.length(); i++) {
            
            char c = css.charAt(i),
                cp = (i > 1) ? css.charAt(i - 1) : '\0',
                cn = (i < css.length() - 1) ? css.charAt(i + 1) : '\0';
            
            if ('\n' == c || 
                '\t' == c || 
                (Character.isWhitespace(c) && ':' == cp) || 
                (Character.isWhitespace(c) && ',' == cp) ||
                (Character.isWhitespace(c) && '{' == cn))
            {
                continue;
            }
            
            sb.append(c);

        }
        
        css = sb.toString();
        sb.setLength(0);
        
        for (int i = 0; i < css.length(); i++) {
            
            char c = css.charAt(i),
                cn = (i < css.length() - 1) ? css.charAt(i + 1) : '\0';
            
            if (';' == c && '}' == cn) {
                continue;
            }
            
            sb.append(c);

        }
        
        return sb.toString();
        
    }
    
    private static String _getLvlTabulation(int lvl) {
        
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < lvl; i++) {
            sb.append('\t');
        }
        
        return sb.toString();
                
    }
    
    public List<CssRule> getRulesForId(String elemId) {
        
        List<CssRule> rules = new ArrayList<>();
        
        if (!elemId.startsWith("#")) {
            
            elemId = "#" + elemId;
            
        }
        
        for (CssRule rule : this._cssRules) {
            
            if (rule.getSelector().endsWith(elemId)) {
                
                rules.add(rule);
                
            }
            
        }
        
        return rules;
        
    }
    
    public List<CssRule> getRulesForClass(String elemClass) {
        
        List<CssRule> rules = new ArrayList<>();
        
        if (!elemClass.startsWith(".")) {
            
            elemClass = "." + elemClass;
            
        }
        
        for (CssRule rule : this._cssRules) {
            
            if (rule.getSelector().endsWith(elemClass)) {
                
                rules.add(rule);
                
            }
            
        }
        
        return rules;
        
    }
    
    public List<CssRule> getRulesForTag(String elemTag) {
        
        List<CssRule> rules = new ArrayList<>();
        
        for (CssRule rule : this._cssRules) {
            
            if (rule.getSelector().endsWith(elemTag)) {
                
                rules.add(rule);
                
            }
            
        }
        
        return rules;
        
    }
    
    public List<CssRule> getRulesContainProperty(String propertyName) {
        
        List<CssRule> rules = new ArrayList<>();
        
        for (CssRule rule : this._cssRules) {
            
            List<CssProperty> properties = rule.getPropertiesList();
            
            for (CssProperty property : properties) {
                
                if (property.getName().equalsIgnoreCase(propertyName)) {
                    
                    rules.add(rule);
                    
                }
                
            }
            
        }
        
        return rules;
        
    }
    
    public void addRule(CssRule rule) {
        
        this._cssRules.add(rule);
        
    }
    
}