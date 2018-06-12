package com.example.s215131746.driplit;

import java.util.ArrayList;

import viewmodels.PersonModel;
import viewmodels.ResidentUsageModel;
import viewmodels.TipModel;
import viewmodels.UspMobGetPersonItemTotal;
import viewmodels.UspMobGetPersonTotalUsage;

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
