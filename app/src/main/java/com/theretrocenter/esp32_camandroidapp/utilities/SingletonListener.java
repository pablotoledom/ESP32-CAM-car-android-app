package com.theretrocenter.esp32_camandroidapp.utilities;

public class SingletonListener
{
    private static SingletonListener instance;

    private boolean changed = false;
    private ChangeListener listener;

    public static void initInstance()
    {
        if (instance == null)
        {
            // Create the instance
            instance = new SingletonListener();
        }
    }

    public static SingletonListener getInstance()
    {
        // Return the instance
        return instance;
    }

    private SingletonListener()
    {
        // Constructor hidden because this is a singleton
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
        if (listener != null) listener.onChange();
    }

    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public interface ChangeListener {
        void onChange();
    }
}
