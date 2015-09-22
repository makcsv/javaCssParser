
package com.makcsv.css;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Max Soloviov <makcsv@bigmir.net>
 */
public class CssRule {
    
    private String _ruleText;
    
    private final boolean _isAtRule;
    
    private final boolean _hasChildren;
    
    private final String _parentRuleSelector;
    
    private String _selector;
    
    private final int _nestingLvl;
    
    private List<CssProperty> _properties;
    
    public CssRule(String ruleText) {
        
        this._ruleText = ruleText;
        this._isAtRule = false;
        this._hasChildren = this._hasChildren();
        this._parentRuleSelector = "";
        this._nestingLvl = 0;
        
        this._parseRule(ruleText);
        
    }
    
    public CssRule(String ruleText, boolean isAtRule) {
        
        this._ruleText = ruleText;
        this._isAtRule = isAtRule;
        this._hasChildren = this._hasChildren();
        this._parentRuleSelector = "";
        this._nestingLvl = 0;
        
        
        this._parseRule(ruleText);
        
    }
    
    public CssRule(String ruleText, boolean isAtRule, String parentRuleSelector) {
        
        this._ruleText = ruleText;
        this._isAtRule = isAtRule;
        this._hasChildren = this._hasChildren();
        this._parentRuleSelector = parentRuleSelector;
        this._nestingLvl = 1;
        
        this._parseRule(ruleText);
        
    }
    
    public CssRule(String ruleText, boolean isAtRule, String parentRuleSelector, int nestingLvl) {
        
        this._ruleText = ruleText;
        this._isAtRule = isAtRule;
        this._hasChildren = this._hasChildren();
        this._parentRuleSelector = parentRuleSelector;
        this._nestingLvl = nestingLvl;
        
        this._parseRule(ruleText);
        
    }
    
    private void _parseRule(String ruleText) {
        
        this._properties = new ArrayList<>();
        
        ruleText = _clearRuleText(ruleText);
        
        int bodyStartPos = ruleText.indexOf("{"), bodyEndPos = ruleText.indexOf("}");
        List<String> properiesList;
        
        if (bodyStartPos > -1 && bodyEndPos > -1) {
            
            String propertiesText = ruleText.substring(bodyStartPos + 1, bodyEndPos);
            properiesList = _parseRuleProperties(propertiesText);
            
            this._selector = ruleText.substring(0, bodyStartPos).trim().replace('\n', ' ');
            
            for (String propertyText : properiesList) {
                
                this._properties.add(new CssProperty(propertyText));
                
            }
            
        } else {
            
            bodyStartPos = ruleText.indexOf(" ");
            String propertyText = ruleText.substring(bodyStartPos + 1);
            
            this._selector = ruleText.substring(0, bodyStartPos);
            this._properties.add(new CssProperty(propertyText));
            
        }
        
    }
    
    private String _clearRuleText(String ruleText) {
        
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < ruleText.length(); i++) {
            
            char c = ruleText.charAt(i);
            char nextC = (i < ruleText.length() - 1) ? ruleText.charAt(i + 1) : '\0';
            
            if ('\n' == c || (Character.isWhitespace(c) && Character.isWhitespace(nextC))) {
                continue;
            }
            
            sb.append(c);
            
            if (',' == c && ' ' != nextC) {
                sb.append(' ');
            }
            
        }
        
        return sb.toString();
        
    }
    
    private boolean _hasChildren() {
        
        if (
            this._ruleText.startsWith("@media") ||
            this._ruleText.startsWith("@keyframes") ||
            this._ruleText.startsWith("@supports") ||
            this._ruleText.startsWith("@document")
        ) {
            
            return true;
            
        } else {
            
            return false;
            
        }
        
    }
    
    private List<String> _parseRuleProperties(String properiesText) {
        
        List<String> properties = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean write = false;
        
        for (int i = 0; i < properiesText.length(); i++) {
            
            char c = properiesText.charAt(i);
            
            if (!Character.isWhitespace(c)) {
                write = true;
            }
            
            if (write) {
                sb.append(c);
            }
            
            if (';' == c || i == properiesText.length() - 1) {
                
                String propertyText = sb.toString();
                
                if (!propertyText.isEmpty()) {
                    
                    properties.add(propertyText);
                    
                }
                
                sb.setLength(0);
                write = false;
                
            }
            
        }
        
        return properties;
        
    }
    
    public boolean isAtRule() {
        
        return this._isAtRule;
        
    }
    
    public boolean hasChildren() {
        
        return this._hasChildren;
        
    }
    
    public String asText() {
        
        return this._ruleText;
        
    }
    
    public String getParentRuleSelector() {
        
        return this._parentRuleSelector;
        
    }
    
    public String getSelector() {
        
        return this._selector;
        
    }
    
    public List<CssProperty> getPropertiesList() {
        
        return this._properties;
        
    }
    
    public List<CssProperty> getPropertiesByName(String propName) {
        
        List<CssProperty> props = new ArrayList<>();
        
        for (CssProperty prop : this._properties) {
            
            if (prop.getName().equalsIgnoreCase(propName)) {
                
                props.add(prop);
                
            }
            
        }
        
        return props;
        
    }
    
    public int getNestingLvl() {
        
        return this._nestingLvl;
        
    }
    
    public void addProperty(String propertyName, String propertyValue) {
        
        this._properties.add(new CssProperty(propertyName, propertyValue));
        
    }
    
    public void addProperties(List<CssProperty> properties) {
        
        this._properties.addAll(properties);
        
    }
    
    public void removeProperty(CssProperty property) {
        
        this._properties.remove(property);
        
    }
    
    public void removeProperties(List<CssProperty> properties) {
        
        this._properties.removeAll(properties);
        
    }
    
}