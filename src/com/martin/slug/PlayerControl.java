package com.martin.slug;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.PositionComponent;
import com.almasb.fxgl.entity.view.EntityView;
import com.almasb.fxgl.extra.entity.components.OffscreenCleanComponent;
import com.almasb.fxgl.extra.entity.components.ProjectileComponent;
import com.martin.slug.BasicGameApp;
import com.martin.slug.BulletComponent;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import com.martin.slug.BasicGameApp;

public class PlayerControl extends Component {
    @Override
    public void onUpdate(double tpf) {

    }

    public void shoot(Point2D direction) {
        Entity bullet = new Entity();
        bullet.getTypeComponent().setValue(BasicGameApp.EntityType.BULLET);
        bullet.getPositionComponent().setValue(getEntity().getComponent(PositionComponent.class).getValue().add(20, 20));
        bullet.getViewComponent().setView(new EntityView(new Rectangle(10, 2, Color.BLACK)), true);

        bullet.addComponent(new CollidableComponent(true));
        bullet.addComponent(new OffscreenCleanComponent());
        bullet.addComponent(new ProjectileComponent(direction, 10 * 160));

        BulletComponent bulletData = new BulletComponent();
        bulletData.setDamage(1);
        bulletData.setHp(1);
        bulletData.setSpeed(10);

        bullet.addComponent(bulletData);

        getEntity().getWorld().addEntity(bullet);
    }
}
