package com.example.arcanavault;

import android.content.ClipData;

import com.example.arcanavault.model.data.IItem;
import com.example.arcanavault.model.data.Spell;
import java.util.ArrayList;
import java.util.List;

public class AppState {
    private List<Spell> listOfSpells;

    public AppState() {
        this.listOfSpells = new ArrayList<>();
    }

    public List<Spell> getListOfSpells() {
        return listOfSpells;
    }

    public void setListOfSpells(List<Spell> spells) {
        this.listOfSpells = spells;
    }

    public void setSpellToFavorite(IItem item) {
        item.setFavorite(!item.isFavorite());
    }

    public Spell getSpellByIndex(String index) {
        for (Spell spell : listOfSpells) {
            if (spell.getIndex().equals(index)) {
                return spell;
            }
        }
        return null; // Return null if not found
    }

    public void updateSpellFavoriteStatus(String index, boolean isFavorite) {
        for (Spell spell : listOfSpells) {
            if (spell.getIndex().equals(index)) {
                spell.setFavorite(isFavorite);
                break;
            }
        }
    }


}