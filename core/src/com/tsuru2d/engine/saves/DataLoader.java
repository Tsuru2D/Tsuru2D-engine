package com.tsuru2d.engine.saves;

import java.util.List;

public abstract class DataLoader {
    public abstract void save(GameSaveData data);
    public abstract List<GameSaveData> enumerate();
    public abstract GameSaveData load();
}
