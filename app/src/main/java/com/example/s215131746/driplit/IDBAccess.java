package com.example.s215131746.driplit;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IDBAccess {
    //Single
    PersonModel LoginPerson(PersonModel person);
    //List
    ArrayList<ItemUsageModel> GetItems();
    ArrayList<TipModel> GetTips();
    //aggregates
    float MobGetPersonItemTotal(String userEmail,int itemID);
    //Modifiers
    boolean MobAddResidentUsage(ResidentUsageModel ResUsage);
    boolean MobAddPerson(PersonModel person );
    boolean MobDeletePerson(String email);
    boolean MobPostTip(TipModel tip);
    boolean MobUpdatePerson(PersonModel person);
}
