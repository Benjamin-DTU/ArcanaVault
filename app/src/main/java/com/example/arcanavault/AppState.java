package com.example.arcanavault;

import android.content.ClipData;

import com.example.arcanavault.model.data.Condition;
import com.example.arcanavault.model.data.IItem;
import com.example.arcanavault.model.data.Rule;
import com.example.arcanavault.model.data.Spell;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppState {
    private List<Spell> listOfSpells;
    private List<Condition> listOfConditions;
    private List<Rule> listOfRules;

    private Map<String, List<String>> selectedFilters = new HashMap<>();
    private String searchQuery = "";

    private String sortOption = "Name";
    private boolean sortOrderAscending = true;

    private int savedScrollPosition = 0;
    private int savedFavScrollPosition = 0;

    public AppState() {
        this.listOfSpells = new ArrayList<>();
    }

    public List<Spell> getListOfSpells() {
        return listOfSpells;
    }
    public List<Rule> getListOfRules() {
        return listOfRules;
    }
    public List<Condition> getListOfConditions() {
        return listOfConditions;
    }



    public void setListOfSpells(List<Spell> spells) {
        this.listOfSpells = spells;
    }
    public void setListOfRules(List<Rule> rules) {
        this.listOfRules = rules;
    }
    public void setListOfCondition(List<Condition> conditions) {
        this.listOfConditions = conditions;
    }

    public void setSpellToFavorite(IItem item) {
        item.setFavorite(!item.isFavorite());
    }

    public Spell getSpellByIndex(String index) {
        return listOfSpells.stream().filter(spell -> spell.getIndex().equals(index)).findFirst().orElse(null);
    }

    public Condition getConditionByName(String name) {
        var conditions = getListOfConditions();
        if (conditions == null || name == null) {
            return null;
        }
        return listOfConditions.stream()
                .filter(condition -> condition.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
    public Rule getRuleByName(String name) {
        var rules = getListOfRules();
        if (rules == null || name == null) {
            return null;
        }
        return listOfRules.stream()
                .filter(rule -> rule.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public void updateSpellFavoriteStatus(String index, boolean isFavorite) {
        for (Spell spell : listOfSpells) {
            if (spell.getIndex().equals(index)) {
                spell.setFavorite(isFavorite);
                break;
            }
        }
    }

    public Map<String, List<String>> getSelectedFilters() {
        return selectedFilters;
    }

    public void setSelectedFilters(Map<String, List<String>> filters) {
        this.selectedFilters = filters;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String query) {
        this.searchQuery = query;
    }

    public String getSortOption() {
        return sortOption;
    }

    public void setSortOption(String sortOption) {
        this.sortOption = sortOption;
    }

    public boolean getSortOrderAscending() {
        return sortOrderAscending;
    }

    public void setSortOrderAscending(boolean sortOrderAscending) {
        this.sortOrderAscending = sortOrderAscending;
    }

    public int getSavedScrollPosition() {
        return savedScrollPosition;
    }
    public int getFavSavedScrollPosition() {
        return savedFavScrollPosition;
    }

    public void setSavedScrollPosition(int position) {
        this.savedScrollPosition = position;
    }
    public void setFavSavedScrollPosition(int position) {
        this.savedFavScrollPosition = position;
    }
}
