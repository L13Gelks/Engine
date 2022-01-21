package Game.entity;

import components.Component;

public class Entity extends Component {
    private transient boolean isDead;
    //Stats
    private int health;
    private int defense;
    private int stamina;
    private int strength;
    private int dexterity;
    private int magic;
    private int intelligence;
    private int resistance;

    private float healthPoints;
    private float staminaPoints;
    private float manaPoints;
    private transient float maxHealthPoints;
    private transient float maxStaminaPoints;
    private transient float maxManaPoints;
    private transient float speedX;
    private transient float speedY;
    private transient float invisibleFrames;
    private transient float invisibleFramesLeft;
    private transient float carryCapacity;

    private transient float physAttack;
    private transient float magicAttack;
    private transient float physDefense;
    private transient float magicDefense;
    private transient float physShieldStrength;
    private transient float magicShieldStrength;
    private transient float physShieldPoints;
    private transient float magicShieldPoints;
    private transient float attackSpeed;

    //RESISTANCES
    private transient float slashResistance;
    private transient float bluntResistance;
    private transient float pierceResistance;
    private transient float magicResistance;

    private transient float fireResistance;
    private transient float waterResistance;
    private transient float electricityResistance;
    private transient float airResistance;
    private transient float earthResistance;
    private transient float darkResistance;
    private transient float lightResistance;

    private transient float bleedingResistance;
    private transient float poisonResistance;
    private transient float stunResistance;

    public void receiveDamage(float damage) {
        this.setHealthPoints(this.getHealthPoints() - damage);
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public float getMaxHealthPoints() {
        return maxHealthPoints;
    }

    public void setMaxHealthPoints(float maxHealthPoints) {
        this.maxHealthPoints = maxHealthPoints;
    }

    public float getMaxStaminaPoints() {
        return maxStaminaPoints;
    }

    public void setMaxStaminaPoints(float maxStaminaPoints) {
        this.maxStaminaPoints = maxStaminaPoints;
    }

    public float getMaxManaPoints() {
        return maxManaPoints;
    }

    public void setMaxManaPoints(float maxManaPoints) {
        this.maxManaPoints = maxManaPoints;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getResistance() {
        return resistance;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }

    public float getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(float healthPoints) {
        this.healthPoints = healthPoints;
    }

    public float getStaminaPoints() {
        return staminaPoints;
    }

    public void setStaminaPoints(float staminaPoints) {
        this.staminaPoints = staminaPoints;
    }

    public float getManaPoints() {
        return manaPoints;
    }

    public void setManaPoints(float manaPoints) {
        this.manaPoints = manaPoints;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public float getInvisibleFrames() {
        return invisibleFrames;
    }

    public void setInvisibleFrames(float invisibleFrames) {
        this.invisibleFrames = invisibleFrames;
    }

    public float getInvisibleFramesLeft() {
        return invisibleFramesLeft;
    }

    public void setInvisibleFramesLeft(float invisibleFramesLeft) {
        this.invisibleFramesLeft = invisibleFramesLeft;
    }

    public float getCarryCapacity() {
        return carryCapacity;
    }

    public void setCarryCapacity(float carryCapacity) {
        this.carryCapacity = carryCapacity;
    }

    public float getPhysAttack() {
        return physAttack;
    }

    public void setPhysAttack(float physAttack) {
        this.physAttack = physAttack;
    }

    public float getMagicAttack() {
        return magicAttack;
    }

    public void setMagicAttack(float magicAttack) {
        this.magicAttack = magicAttack;
    }

    public float getPhysDefense() {
        return physDefense;
    }

    public void setPhysDefense(float physDefense) {
        this.physDefense = physDefense;
    }

    public float getMagicDefense() {
        return magicDefense;
    }

    public void setMagicDefense(float magicDefense) {
        this.magicDefense = magicDefense;
    }

    public float getPhysShieldStrength() {
        return physShieldStrength;
    }

    public void setPhysShieldStrength(float physShieldStrength) {
        this.physShieldStrength = physShieldStrength;
    }

    public float getMagicShieldStrength() {
        return magicShieldStrength;
    }

    public void setMagicShieldStrength(float magicShieldStrength) {
        this.magicShieldStrength = magicShieldStrength;
    }

    public float getPhysShieldPoints() {
        return physShieldPoints;
    }

    public void setPhysShieldPoints(float physShieldPoints) {
        this.physShieldPoints = physShieldPoints;
    }

    public float getMagicShieldPoints() {
        return magicShieldPoints;
    }

    public void setMagicShieldPoints(float magicShieldPoints) {
        this.magicShieldPoints = magicShieldPoints;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public float getSlashResistance() {
        return slashResistance;
    }

    public void setSlashResistance(float slashResistance) {
        this.slashResistance = slashResistance;
    }

    public float getBluntResistance() {
        return bluntResistance;
    }

    public void setBluntResistance(float bluntResistance) {
        this.bluntResistance = bluntResistance;
    }

    public float getPierceResistance() {
        return pierceResistance;
    }

    public void setPierceResistance(float pierceResistance) {
        this.pierceResistance = pierceResistance;
    }

    public float getMagicResistance() {
        return magicResistance;
    }

    public void setMagicResistance(float magicResistance) {
        this.magicResistance = magicResistance;
    }

    public float getFireResistance() {
        return fireResistance;
    }

    public void setFireResistance(float fireResistance) {
        this.fireResistance = fireResistance;
    }

    public float getWaterResistance() {
        return waterResistance;
    }

    public void setWaterResistance(float waterResistance) {
        this.waterResistance = waterResistance;
    }

    public float getElectricityResistance() {
        return electricityResistance;
    }

    public void setElectricityResistance(float electricityResistance) {
        this.electricityResistance = electricityResistance;
    }

    public float getAirResistance() {
        return airResistance;
    }

    public void setAirResistance(float airResistance) {
        this.airResistance = airResistance;
    }

    public float getEarthResistance() {
        return earthResistance;
    }

    public void setEarthResistance(float earthResistance) {
        this.earthResistance = earthResistance;
    }

    public float getDarkResistance() {
        return darkResistance;
    }

    public void setDarkResistance(float darkResistance) {
        this.darkResistance = darkResistance;
    }

    public float getLightResistance() {
        return lightResistance;
    }

    public void setLightResistance(float lightResistance) {
        this.lightResistance = lightResistance;
    }

    public float getBleedingResistance() {
        return bleedingResistance;
    }

    public void setBleedingResistance(float bleedingResistance) {
        this.bleedingResistance = bleedingResistance;
    }

    public float getPoisonResistance() {
        return poisonResistance;
    }

    public void setPoisonResistance(float poisonResistance) {
        this.poisonResistance = poisonResistance;
    }

    public float getStunResistance() {
        return stunResistance;
    }

    public void setStunResistance(float stunResistance) {
        this.stunResistance = stunResistance;
    }
}
