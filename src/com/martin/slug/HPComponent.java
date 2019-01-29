package com.martin.slug;

import com.almasb.fxgl.entity.components.IntegerComponent;

public class HPComponent extends IntegerComponent {
    public HPComponent(int hp) {
        super(hp);
    }

    public void increment(int value) {
        setValue(getValue() + value);
    }

    public void decrement(int value) {
        increment(-value);
    }
}