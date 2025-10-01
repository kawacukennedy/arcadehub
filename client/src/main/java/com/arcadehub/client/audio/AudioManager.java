package com.arcadehub.client.audio;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages all game/UI sounds and volume control.
 */
public class AudioManager {
    private Map<String, Sound> soundEffects = new HashMap<>();
    private float volume = 1.0f;

    /**
     * Plays sound effect, supports overlapping sounds.
     */
    public void play(String soundName) {
        // Placeholder: Load and play sound effect
        // Sound sound = soundEffects.get(soundName);
        // if (sound != null) { sound.play(volume); }
        System.out.println("Playing sound: " + soundName);
    }

    /**
     * Stops sound effect if playing.
     */
    public void stop(String soundName) {
        // Placeholder: Stop sound effect
        // Sound sound = soundEffects.get(soundName);
        // if (sound != null) { sound.stop(); }
        System.out.println("Stopping sound: " + soundName);
    }

    /**
     * Adjusts master volume for all sounds.
     */
    public void setVolume(float volume) {
        this.volume = volume;
        // Placeholder: Adjust volume of all playing sounds
        System.out.println("Setting master volume to: " + volume);
    }
}
