package exnihiloomnia.items.sieveassist;

public interface ISieveFaster {
    float getSpeedModifier();

    int getSiftTime();
    void setSiftTime(int siftTime);
    void addSiftTime(int time);
}
