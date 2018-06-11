package com.example.s215131746.driplit;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IDBAccess {
    //Single
    PersonModel LoginPerson(PersonModel person);
    ArrayList<ItemUsageModel> GetItems();
    ArrayList<UspMobGetPersonItemTotal> UspMobGetPersonItemTotal(String email);
    ArrayList<UspMobGetPersonTotalUsage> GetPersonTotalUsageGetItems(String email);
    ArrayList<UspMobGetPersonItemTotal> uspMobGetPersonItemTotal(String userEmail);
    ArrayList<UspMobGetPersonItemTotal> uspMobGetPersonItemTotalDate(String userEmail, String date);
    ArrayList<TipModel> GetTips();
    boolean uspMobUpdatePerson(PersonModel person );
    boolean MobAddPerson(PersonModel person );
    boolean MobAddTip(TipModel tip );
    boolean MobDeletePerson(String email);
    boolean MobAddResidentUsage(ResidentUsageModel ResUsage);
}
