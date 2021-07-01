package model.role.state;

public abstract class DecideMove extends NotNormal {
    @Override
    public boolean decidable(){
        return true;
    }
}
