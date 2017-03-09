import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Main {

    public static void main(String[] args) throws Exception {
        File file = new File("qrcodeurl.txt");
        FileOperation.createFile(file);
        String result = "";
        String badResult = "";
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            File fileName = new File("erweimaurl.txt");
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            String[] apikey = {"20170221165513", "20170309135206", "20170309131411", "20170309137198", "20170309136996",
                    "20170309139621", "20170309133441", "20170309136234", "20170309137782", "20170309138567"};
            try {
                String read = null;
                int i = 0;
                while ((read = bufferedReader.readLine()) != null && read.length() > 0) {
                    String str = HttpRequest.sendGet("http://api.wwei.cn/dewwei.html",
                            "apikey=" + apikey[i] + "&data=" + read);
                    JSONObject object = new JSONObject(str);
                    if (object != null && object.getInt("status") == 1) {
                        JSONObject object2 = object.getJSONObject("data");
                        if (object2 != null) {
                            if (object2.getString("raw_text") != null) {
                                System.out.println(object2.getString("raw_text"));
                                String newRed = read.replace("http://cardfile.52yq.cn/qrcode/", "");
                                newRed = newRed.substring(0, newRed.indexOf(".jpg"));
                                result = result + object2.getString("raw_text") + "," + read + "," + newRed + "\r\n";
                            }
                        }
                    } else {
                        badResult = badResult + read + "\r\n";
                    }
                    if (i < 9) {
                        i++;
                    } else {
                        i = 0;
                    }
                    Thread.sleep(150);
                }
                FileOperation.writeTxtFile(result + "\r\n\r\n\r\n解析失败\r\n" + badResult, file);
                System.out.println("解析完成");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }
        }
    }
}
