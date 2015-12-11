/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import jAudioFeatureExtractor.ACE.DataTypes.Batch;
import jAudioFeatureExtractor.ACE.XMLParsers.XMLDocumentParser;
import jAudioFeatureExtractor.CommandLineThread;
import jAudioFeatureExtractor.DataModel;
import jAudioFeatureExtractor.DataTypes.RecordingInfo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.MutableComboBoxModel;
import weka.classifiers.Classifier;

/**
 *
 * @author mick
 */
public class GlobalData {
    public static MutableComboBoxModel classifierList;
    public static ArrayList<Classifier> classierList = new ArrayList<Classifier>();
    
    public static String getFeatures(String directoryName) throws FileNotFoundException, Exception{
        
        DataModel dm = new DataModel("my_features.xml",null);
        dm.featureKey = new FileOutputStream(new File("definitions.arff"));
        dm.featureValue = new FileOutputStream(new File(directoryName + ".arff"));
        
        File dir = new File(directoryName);
        File[] directoryListing = dir.listFiles();
        RecordingInfo[] recording_info = new RecordingInfo[directoryListing.length];
        int i = 0;
        for (File child : directoryListing) {
            recording_info[i++] = new RecordingInfo(child.getName(),child.getPath(),null,false);
        }
         
        Object[] data = (Object[]) XMLDocumentParser.parseXMLDocument("blankBatch.xml","batchFile");
        
        Batch b = (Batch)data[0];
       
        b.setDataModel(dm);
        b.setRecording(recording_info);
        b.setOutputType(1);
        
        try{
            CommandLineThread clt = new CommandLineThread(b);
            clt.start();
            while(clt.isAlive()){
                if(System.in.available() > 0){
                    clt.cancel();
                }
                clt.join(1000);
            }
        } catch (Exception e) {
            System.out.println("EXTRACTION FAILED: something went wrong");
            System.out.println(e.getMessage());
            System.exit(5);
        }
  
        return directoryName + ".arff";
    }

}


