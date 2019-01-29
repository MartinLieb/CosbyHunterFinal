package com.martin.slug;

import com.almasb.fxgl.entity.component.Component;

public class BulletComponent extends Component{

    private int damage;
    private double speed;
    private int hp;

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
