package com.martin.slug;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.PositionComponent;
import javafx.geometry.Point2D;

public class EnemyControl extends Component {

    private Point2D target;

    public EnemyControl(Point2D target) {
        this.target = target;
    }

    @Override
    public void onUpdate(double tpf) {
        Point2D position = entity.getComponent(PositionComponent.class).getValue();

        entity.getComponent(PositionComponent.class).translate(target.subtract(position).normalize().multiply(60 * tpf));
    }
}