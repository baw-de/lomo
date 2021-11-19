/*
 * Copyright (c) 2021 Bundesanstalt für Wasserbau
 *
 * This file is part of LoMo.
 *
 * LoMo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * LoMo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LoMo.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.baw.lomo.gui;

import java.math.BigInteger;

import javafx.beans.binding.NumberExpression;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.IndexRange;
import javafx.scene.control.TextField;

/*
 * TODO replace this with proper API when it becomes available:
 * https://javafx-jira.kenai.com/browse/RT-30881
 */
class NumericField extends TextField {

    private final NumericValidator<? extends Number> value ;
    		
    public NumericField( Class<? extends Number> cls ) {
    	
    	if ( cls == byte.class || cls == Byte.class || cls == short.class || cls == Short.class ||
    		 cls ==	int.class  || cls == Integer.class || cls == long.class || cls == Long.class ||
    	     cls == BigInteger.class) {
    		value = new LongValidator(this);
    	} else {
    		value = new DoubleValidator(this);
    	}
    	
      textProperty().addListener((observable) -> {
        value.setValue(value.toNumber(getText()));
      });
        
    }
    
    public final ObservableValue<Number> valueProperty() {
        return value;
    }

    @Override public void replaceText(int start, int end, String text) {
        if (replaceValid(start, end, text)) {
            super.replaceText(start, end, text);
        }
    }

    @Override public void replaceSelection(String text) {
        IndexRange range = getSelection();
        if (replaceValid(range.getStart(), range.getEnd(), text)) {
            super.replaceSelection(text);
        }
    }

    private Boolean replaceValid(int start, int end, String fragment) {
        try {
        	String newText = getText().substring(0, start) + fragment + getText().substring(end);
        	if (newText.isEmpty()) return true; 
			value.toNumber(newText);
        	return true;
        } catch( Throwable ex ) {
        	return false;
        }
    }
    
    
    private static abstract interface NumericValidator<T extends Number> extends NumberExpression {
    	void setValue(Number num);
    	T toNumber(String s);
    	
    }
    
    static class DoubleValidator extends SimpleDoubleProperty implements NumericValidator<Double>{
    	
    	private NumericField field;
    	
    	public DoubleValidator(NumericField field) {
    		super(field, "value", 0.0); //$NON-NLS-1$
    		this.field = field;
		}
    	
    	@Override protected void invalidated() {
    		
    		String number = Double.toString(get());
            
            if(field.getCharacters().length()==0) {
            	
            	number = number.replaceAll("\\.?0+$", "");
            	field.setText(number);
            }
    		
    		
            
        }

		@Override
		public Double toNumber(String s) {
			if ( s == null || s.trim().isEmpty() ) return 0d;
	    	String d = s.trim();
	    	if ( d.endsWith("f") || d.endsWith("d") || d.endsWith("F") || d.endsWith("D") ) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	    		throw new NumberFormatException("There should be no alpha symbols"); //$NON-NLS-1$
	    	}
	    	return new Double(d);
		};
		
    }
 
    
    static class LongValidator extends SimpleLongProperty implements NumericValidator<Long>{
    	
    	private NumericField field;
    	
    	public LongValidator(NumericField field) {
    		super(field, "value", 0l); //$NON-NLS-1$
    		this.field = field;
		}
    	
    	@Override protected void invalidated() {
            field.setText(Long.toString(get()));
        }

		@Override
		public Long toNumber(String s) {
			if ( s == null || s.trim().isEmpty() ) return 0l;
	    	String d = s.trim();
	    	return new Long(d);
		};
		
    }    
    
    
}
