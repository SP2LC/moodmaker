package sp2lc.application.moodmaker;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class ReadFile {
    InputStream is = null;
    BufferedReader br = null;


    public ReadFile(InputStream inputStream) {
        is = inputStream;
    }

    public ArrayList<Data> Read() {
        String str;
        final ArrayList<Data> list = new ArrayList<Data>();
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((str = br.readLine()) != null) {
                String temp[] = str.split(",", 0);
                Data data = new Data();
                data.r = temp[0];
                data.g = temp[1];
                data.b = temp[2];
                data.time = temp[3];
                list.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

}

