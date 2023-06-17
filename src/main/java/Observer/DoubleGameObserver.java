package Observer;

import Being.Role;
import Gui.Frame.DoubleGameFrame;
import Gui.Frame.GameFrame;
import Gui.Label.*;

import java.util.ArrayList;

public class DoubleGameObserver extends Observer{

    protected ArrayList<CreatureLabel> creatureLabels = new ArrayList<>();
    protected ArrayList<RoleLabel> roleLabels = new ArrayList<>();

    public DoubleGameObserver(GameFrame gameFrame) {
        super(gameFrame);
    }

    @Override
    public void addCreature(CreatureLabel creatureLabel) {
        this.creatureLabels.add(creatureLabel);
    }

    @Override
    public void addRole(RoleLabel roleLabel) {
        this.roleLabels.add(roleLabel);
    }

    @Override
    public void bulletNotify(BulletLabel bulletLabel) {
       if(bulletLabel.getLauncher()){
            if(this.hitCreature(bulletLabel)) {
                bulletLabel.disappear();
            }
        }
        else if(!bulletLabel.getLauncher()){
            if(this.hitRole(bulletLabel)){
                bulletLabel.disappear();
            }
        }

    }

    private boolean hitCreature(BulletLabel bulletLabel){
        if(bulletLabel.getLauncher()){
            for(int i = 0; i < creatureLabels.size(); i++){
                DoubleCreatureLabel creatureLabel = (DoubleCreatureLabel) creatureLabels.get(i);
                if(creatureLabel.hitByBullet((DoubleBulletLabel)bulletLabel)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hitRole(BulletLabel bulletLabel){
        for(int i = 0; i < this.roleLabels.size(); i++){
            if(!bulletLabel.getLauncher()){
                if(roleLabels.get(i).hitByBullet(bulletLabel)){
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public void creatureNotify(CreatureLabel creatureLabel) {
        for(int i = 0; i < roleLabels.size(); i++){
            RoleLabel target = this.roleLabels.get(i);
            int t_x = target.get_x();
            int t_y = target.get_y();
            int x = creatureLabel.getCur_x() / creatureLabel.getHeight();
            int y = creatureLabel.getCur_y() / creatureLabel.getWidth();
            int dx = t_x - x;
            int dy = t_y - y;
            int jd = dx * dx + dy * dy;
            if(jd <= 1){
                Role role = target.getRole();
                role.setHp(role.getHp() - creatureLabel.getCreatureAtk());
                target.updateHp();
            }
        }
    }

    @Override
    public void roleNotify(RoleLabel roleLabel) {
        for(int i = 0; i < this.creatureLabels.size(); i++){
            DoubleCreatureLabel doubleCreatureLabel = (DoubleCreatureLabel) creatureLabels.get(i);
            int dx = roleLabel.get_x() - doubleCreatureLabel.get_x();
            int dy = roleLabel.get_y() - doubleCreatureLabel.get_y();
            int distance = dx * dx + dy * dy;
            if(distance <= 1){
                DoubleRoleLabel doubleRoleLabel = (DoubleRoleLabel)roleLabel;
                int index = doubleRoleLabel.getIndex();
                doubleCreatureLabel.getAtk(index);
            }
        }
    }

    @Override
    public void roleDied(RoleLabel roleLabel) {
        DoubleGameFrame doubleGameFrame = (DoubleGameFrame) this.gameFrame;
        doubleGameFrame.killRole();
    }

    @Override
    public void creatureDied(CreatureLabel creatureLabel) {
        this.gameFrame.killMonster();
    }
}
