package com.thinkgem.jeesite.common.service.converter;



import com.thinkgem.jeesite.common.config.Constant;
import com.thinkgem.jeesite.common.utils.FileUtils;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created with IntelliJ IDEA.
 * User: meng.zm
 * Date: 14-3-20
 * Time: 下午10:32
 * To change this template use File | Settings | File Templates.
 */

public class OfficeConverter extends AbstractConverter{

    private static OfficeManager officeManager;
    private static String OFFICE_HOME = Constant.OFFICE_HOME;
    private static boolean isStart = false;
    private static int port = Constant.OFFICE_PORT;



    public  void convert2PDF(String inputFile, String pdfFile) {

        if(inputFile.endsWith(".txt")){
            String odtFile = FileUtils.getFilePrefix(inputFile)+".odt";
            if(new File(odtFile).exists()){
                System.out.println("odt文件已存在！");
                inputFile = odtFile;
            }else{
                try {
                    FileUtils.copyFile(inputFile,odtFile);
                    inputFile = odtFile;
                } catch (Exception e) {
                    System.out.println("文档不存在！");
                    e.printStackTrace();
                }
            }
        }

        startService();
        System.out.println("进行文档转换转换:" + inputFile + " --> " + pdfFile);
        OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
        converter.convert(new File(inputFile),new File(pdfFile));
        stopService();
        System.out.println();
    }


    public void convert2PDF(String inputFile) {
        String pdfFile = FileUtils.getFilePrefix(inputFile)+".pdf";
        convert2PDF(inputFile,pdfFile);

    }

    public static void startService(){
        DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
        //System.out.println(BRWebApplicationContext.getWebRootPath());
        //OFFICE_HOME = BRWebApplicationContext.getWebRootPath().replace("upload","OpenOffice4");
        //OFFICE_HOME = "C:\\Program Files (x86)\\OpenOffice 4";
        try {
            System.out.println("准备启动服务....");
            configuration.setOfficeHome(OFFICE_HOME);//设置OpenOffice.org安装目录
            configuration.setPortNumbers(port); //设置转换端口，默认为8100
            configuration.setTaskExecutionTimeout(1000 * 60 * 5L);//设置任务执行超时为5分钟
            configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);//设置任务队列超时为24小时

            officeManager = configuration.buildOfficeManager();
            officeManager.start();    //启动服务
            System.out.println("office转换服务启动成功!");
            isStart = true;
        } catch (Exception ce) {
            System.out.println("office转换服务启动失败!详细信息:" + ce);
        }
    }

    public static void stopService(){
        isStart = false;
        System.out.println("关闭office转换服务....");
        if (officeManager != null) {
            officeManager.stop();
        }
        System.out.println("关闭office转换成功!");
    }

    @Override
    public String File2Swf(String inputFullFile,String outputFile) throws Exception {
       /* String pdfRootPath = BRWebApplicationContext.getWebRootPath() + File.separator + "pdf" ;
        File file = new File(pdfRootPath);
        if(!file.exists()){
            file.mkdirs();
        }
        String pdfPath = pdfRootPath + File.separator+id+".pdf";
        this.convert2PDF(path,pdfPath);
        ConverterContext.convert("pdf",pdfPath,id);
        return pdfRootPath;*/
       return null;
    }
    public static boolean isStart(){
        return isStart;
    }

    public static  void main( String[] args){
        OfficeConverter officeConverter=new OfficeConverter();
        officeConverter.convert2PDF("D:\\upload\\contract.doc","D:\\upload\\contract.pdf");
    }
}
