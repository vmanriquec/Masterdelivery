package com.mazinger.masterdelivery.Realm;

import android.util.Log;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Crudiniciodeldia {
  
  public final static int calculateIndex(){
          Realm realm = Realm.getDefaultInstance();
          Number currentIdNum = realm.where(IniciodeldiaRealm.class).max("idiniciodeldia");
          int nextId;
          if(currentIdNum == null){
              nextId = 0;
          }else {
              nextId = currentIdNum.intValue()+1;
          }
          return nextId;
      }
  
  
      public final static void addIniciodeldiarealm(final IniciodeldiaRealm Iniciodeldiarealm){
          Realm realm = Realm.getDefaultInstance();
          realm.executeTransaction(new Realm.Transaction(){
              @Override
              public void execute(Realm realm){
                  int index = Crudiniciodeldia.calculateIndex();
                  IniciodeldiaRealm iniciodeldiaRealm = realm.createObject(IniciodeldiaRealm.class, index);
                  iniciodeldiaRealm.setIdestados(iniciodeldiaRealm.getIdestados());

                  iniciodeldiaRealm.setFechainicial( iniciodeldiaRealm.getFechainicial());
                  iniciodeldiaRealm.setIdalmacen(iniciodeldiaRealm.getIdalmacen());
                  iniciodeldiaRealm.setNumerodedocumento(iniciodeldiaRealm.getNumerodedocumento());
                  iniciodeldiaRealm.setFechadeentrega(iniciodeldiaRealm.getFechadeentrega());
                  iniciodeldiaRealm.setIdtipomovimiento(iniciodeldiaRealm.getIdtipomovimiento());
                    iniciodeldiaRealm.setIdvalidadorentrega(iniciodeldiaRealm.getIdvalidadorentrega());
 iniciodeldiaRealm.setIdvalidadorrecibe(iniciodeldiaRealm.getIdvalidadorrecibe());
 iniciodeldiaRealm.setTotal(iniciodeldiaRealm.getTotal());
                    }
          });
      }
      public final static List<IniciodeldiaRealm> getAllealm(){

          Realm realm = Realm.getDefaultInstance();
          RealmResults<IniciodeldiaRealm> IniciodeldiaRealm = realm.where(IniciodeldiaRealm.class).findAll();
          for(IniciodeldiaRealm iniciorealm: IniciodeldiaRealm){
              Log.d("TAG", "idinicodeldia: " + iniciorealm.getIdiniciodeldia() );
   
          }
          return IniciodeldiaRealm;
      }
  
  
      public final static IniciodeldiaRealm traeriniciodeldiaporid(int idiniciodeldia){
          Realm realm = Realm.getDefaultInstance();
          IniciodeldiaRealm IniciodeldiaRealm = realm.where(IniciodeldiaRealm.class).equalTo("idiniciodeldia", idiniciodeldia).findFirst();
          if(IniciodeldiaRealm != null){
              Log.d("TAG", "nuemro de docuento por id : " + IniciodeldiaRealm.getNumerodedocumento() );
          }
          return IniciodeldiaRealm;
      }
  
      public final static void Actualizartotaldeiniciodeldia(int idiniciodeldia, Double total){
  
          Realm realm = Realm.getDefaultInstance();
          realm.beginTransaction();
          IniciodeldiaRealm IniciodeldiaRealm = realm.where(IniciodeldiaRealm.class).equalTo("idiniciodeldia", idiniciodeldia).findFirst();
          IniciodeldiaRealm.setTotal(total);
   realm.insertOrUpdate(IniciodeldiaRealm);
          realm.commitTransaction();
          Log.d("TAG", "se actualiuzo total : " + Double.toString(total ));
      }
      public final static void Actualizarnumerodedocumento(int idiniciodeldia, int descripcionpedido){
  
          Realm realm = Realm.getDefaultInstance();
          realm.beginTransaction();
          IniciodeldiaRealm IniciodeldiaRealm = realm.where(IniciodeldiaRealm.class).equalTo("descripcionpedido", descripcionpedido).findFirst();
          IniciodeldiaRealm.setNumerodedocumento(descripcionpedido);
          realm.insertOrUpdate(IniciodeldiaRealm);
          realm.commitTransaction();
          Log.d("TAG", "se actualizo documento de inicio: " + descripcionpedido );
      }
  
      public final static void eliminarpedidoydetallesdepedidoporidiniciodeldia(int idiniciodeldia){
          Realm realm = Realm.getDefaultInstance();
          realm.beginTransaction();
  
          realm.executeTransaction(realm1 -> {
              RealmResults<DetalleiniciodeldiaRealm> result = realm1.where(DetalleiniciodeldiaRealm.class).equalTo("idiniciodeldia",idiniciodeldia).findAll();
              result.deleteAllFromRealm();
          });
          Log.d("TAG", "se elimino todo el detalle del dia con id : " + String.valueOf(idiniciodeldia) );
  
  
          IniciodeldiaRealm IniciodeldiaRealm = realm.where(IniciodeldiaRealm.class).equalTo("idiniciodeldia", idiniciodeldia).findFirst();
          IniciodeldiaRealm.deleteFromRealm();
          realm.commitTransaction();
          Log.d("TAG", "se elimino iniciodeldia con id : " + String.valueOf(idiniciodeldia) );
      }
  
  
}




