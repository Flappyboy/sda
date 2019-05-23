package cn.edu.nju.software.sda.app.plugin;

import cn.edu.nju.software.sda.core.utils.FileCompress;
import cn.edu.nju.software.sda.app.utils.command.CommandResult;
import cn.edu.nju.software.sda.app.utils.command.CommandUtils;
import cn.edu.nju.software.sda.core.utils.WorkspaceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.n3r.idworker.Sid;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Properties;

@Slf4j
public class Generate {
    public static final String relativeBasePath = "/plugin";
    public static final String BUILD_BAT_FILE = "/build.bat";
    public static final String BUILD_SHELL_FILE = "/build.sh";
    public static final String START_UP_BAT_FILE = "/startup.bat";
    public static final String START_UP_SHELL_FILE = "/startup.sh";
    public static final String PLUGIN_PROPERTIES_FILE= "/resources/plugin.properties";
    public static final String ALL_CLASS_FILE = "/src/main/resources/allclass.txt";

    public static final String SAVE_JAR_PATH = "/plugin/generate/jar";

    private Properties properties = new Properties();

    private String filePath;

    private String name;

    private String allClassName;

    private String appId;

    private String generateId;

    public Generate(String name, String allClassName, String appId) {
        this(name, appId);
        this.allClassName = allClassName;
    }

    public Generate(String name, String appId) {
        this.name = name;
        this.appId = appId;
        this.filePath = WorkspaceUtil.path();
    }

    public File getJar(){
        File dir = new File(filePath+SAVE_JAR_PATH+"/"+appId);
        if(!dir.exists()){
            return null;
        }
        File[] files = dir.listFiles( new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.endsWith(".jar"))
                    return true;
                return false;
            }
        });
        if(files.length==0){
            return null;
        }
        return files[0];
    }

    private String getGeneratePath(){
        return filePath+relativeBasePath+"/"+ generateId;
    }

    private String getGenerateJarDirPath(){
        return filePath+relativeBasePath+"/"+generateId+"/"+getPluginName()+"/build/dist";
    }

    private File getGenerateJar() {

        File dir = new File(getGenerateJarDirPath());
        if(!dir.exists()){
            return null;
        }
        File[] files = dir.listFiles( new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.endsWith(".jar"))
                    return true;
                return false;
            }
        });
        if(files.length==0){
            return null;
        }
        return files[0];
    }

    public File generateJar(){
        File file = uncompressPluginGenerate();
        setPropertiesFile(file);
        CommandResult commandResult1 = CommandUtils.exec(file.getAbsolutePath()+START_UP_BAT_FILE);
        modifyJavaFile(file);
        writeAllClassFile(file);
        CommandResult commandResult2 = CommandUtils.exec(file.getAbsolutePath()+"/"+getPluginName()+BUILD_BAT_FILE);
        System.out.println(commandResult2.getOutputForString());
        System.out.println(commandResult2.getErrorOutputForString());
        File jar = getGenerateJar();
        saveJar(jar);
//        try {
//            FileUtils.deleteDirectory(file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return null;
    }

    private void saveJar(File jar){
        File dir = new File(filePath+SAVE_JAR_PATH+"/"+appId);
        try {
            if(dir.exists()) {
                FileUtils.cleanDirectory(dir);
            }else{
                dir.mkdirs();
            }
            FileUtils.copyFileToDirectory(jar, dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeAllClassFile(File pluginGenerateDir){
        File file = new File(pluginGenerateDir.getAbsolutePath()+"/"+getPluginName()+ALL_CLASS_FILE);
        try {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(allClassName);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void modifyJavaFile(File pluginGenerateDir){
        File file = new File(pluginGenerateDir.getAbsolutePath()+"/"+getPluginName()+"/src/main/java/com/navercorp/pinpoint/plugin/"+name+"/"+getPluginName()+"Constants.java");
        StringBuilder sb = new StringBuilder();
        try {
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),"UTF-8");//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    sb.append(lineTxt);
                    sb.append("\n");
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sb = addImport(sb);
        sb = modifyStatic(sb);
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(sb.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private StringBuilder addImport(StringBuilder sb){
        int index = sb.indexOf("import");
        return sb.insert(index, "import java.io.*;\n");

    }
    private StringBuilder modifyStatic(StringBuilder sb){
        int index = sb.indexOf("static{");
        sb = sb.replace(index, sb.length(), STATIC_CODE.replace("HapPlugin",getServiceName()));
        return sb;
    }

    private File uncompressPluginGenerate(){
        String id = Sid.next();
        generateId = id;
        //private String pluginGenerateZipFilePath = "E:\\workspace\\project\\pinpoint-plugin-generate\\build\\distributions\\pinpoint-plugin-generate-1.0.5-released.zip";
        File zipFile = null;
        try {
            zipFile = ResourceUtils.getFile("classpath:tools/pinpoint-plugin-generate-1.0.5-released.zip");
            File file = FileCompress.unZip(zipFile, getGeneratePath());
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getServiceName(){
        return name+"Plugin";
    }

    private void setPropertiesFile(File pluginGenerateDir){
        File file = new File(pluginGenerateDir.getAbsolutePath()+PLUGIN_PROPERTIES_FILE);
        FileInputStream iFile = null;
        FileOutputStream oFile = null;
        try {
            iFile = new FileInputStream(file);
            properties.load(iFile);
            properties.setProperty("plugin.author","nju");
            properties.setProperty("plugin.name",getPluginName());
            properties.setProperty("plugin.serviceName",getServiceName());
            properties.setProperty("plugin.package","com.navercorp.pinpoint.plugin."+name);
            properties.setProperty("plugin.interceptor.classes","com.demo|*");

            properties.setProperty("plugin.serviceType","1958");
            properties.setProperty("plugin.eventServiceType","1959");
            properties.setProperty("plugin.argKeyCode","929");
            properties.setProperty("plugin.argKeyName","929.args");


            oFile = new FileOutputStream(file);
            properties.store(oFile, "");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                oFile.close();
                iFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPluginName(){
        return name+"plugin";
    }
static{

}
    public static void main(String[] args) {
        Generate generate = new Generate("jsherp",testallclass, "190216G9CMGFD680");
        generate.generateJar();
    }

    public static final String  STATIC_CODE = "static{\n" +
            "        String[] className = null;\n" +
            "        try {\n" +
            "            InputStream in = HapPluginConstants.class.getResourceAsStream(\"/allclass.txt\");\n"+
            "            byte[] filecontent = new byte[in.available()];\n" +
            "            in.read(filecontent);\n" +
            "            in.close();\n" +
            "            className = new String(filecontent, \"utf-8\").split(\" \");\n" +
            "        } catch (Exception e) {\n" +
            "            e.printStackTrace();\n" +
            "        }\n" +
            "        HapPluginInterceptorClassInfo interceptorClassInfo = null;\n" +
            "        List<HapPluginMethodInfo> methodInfos = null;\n" +
            "        HapPluginMethodInfo methodInfo = null;\n" +
            "        for (int i=0; i<className.length; i++){\n" +
            "            interceptorClassInfo = new HapPluginInterceptorClassInfo();\n" +
            "            interceptorClassInfo.setInterceptorClass(className[i]);\n" +
            "            methodInfo = new HapPluginMethodInfo();\n" +
            "            methodInfo.setFilterType(1);\n" +
            "            methodInfo.setName(\"*\");\n" +
            "            methodInfo.setPattern(true);\n" +
            "            interceptorClassInfo.setAllAccept(methodInfo);\n" +
            "\n" +
            "\n" +
            "            methodInfos = null;\n" +
            "            methodInfos = new ArrayList<HapPluginMethodInfo>();\n" +
            "            methodInfo = new HapPluginMethodInfo();\n" +
            "            methodInfo.setFilterType(1);\n" +
            "            methodInfo.setName(\"*\");\n" +
            "            methodInfo.setPattern(true);\n" +
            "            methodInfos.add(methodInfo);\n" +
            "\n" +
//            "            methodInfo = new HapPluginMethodInfo();\n" +
//            "            methodInfo.setFilterType(0);\n" +
//            "            methodInfo.setName(\"get\");\n" +
//            "            methodInfo.setPattern(true);\n" +
//            "            methodInfos.add(methodInfo);\n" +
//            "            methodInfo = new HapPluginMethodInfo();\n" +
//            "            methodInfo.setFilterType(0);\n" +
//            "            methodInfo.setName(\"set\");\n" +
//            "            methodInfo.setPattern(true);\n" +
//            "            methodInfos.add(methodInfo);\n" +
            "\n" +
            "            interceptorClassInfo.setInterceptorMehtods(methodInfos);\n" +
            "            interceptorClassInfo.setMethodFilter(new HapPluginCustomMethodFilter(null,interceptorClassInfo));\n" +
            "            clazzInterceptors.add(interceptorClassInfo);\n" +
            "        }\n" +
            "    }\n" +
            "}\n";

    public static final String testallclass = "com.jsh.erp.config.WebConfig$FrontEnd com.jsh.erp.config.WebConfig com.jsh.erp.constants.BusinessConstants com.jsh.erp.constants.ExceptionConstants com.jsh.erp.controller.AccountController com.jsh.erp.controller.AccountHeadController com.jsh.erp.controller.AccountItemController com.jsh.erp.controller.AppController com.jsh.erp.controller.DepotController com.jsh.erp.controller.DepotHeadController com.jsh.erp.controller.DepotItemController com.jsh.erp.controller.FunctionsController com.jsh.erp.controller.InOutItemController com.jsh.erp.controller.MaterialCategoryController com.jsh.erp.controller.MaterialController com.jsh.erp.controller.PersonController com.jsh.erp.controller.ResourceController com.jsh.erp.controller.RoleController com.jsh.erp.controller.SerialNumberController com.jsh.erp.controller.SupplierController com.jsh.erp.controller.UserBusinessController com.jsh.erp.controller.UserController com.jsh.erp.datasource.entities.Account com.jsh.erp.datasource.entities.AccountExample$Criteria com.jsh.erp.datasource.entities.AccountExample$Criterion com.jsh.erp.datasource.entities.AccountExample$GeneratedCriteria com.jsh.erp.datasource.entities.AccountExample com.jsh.erp.datasource.entities.AccountHead com.jsh.erp.datasource.entities.AccountHeadExample$Criteria com.jsh.erp.datasource.entities.AccountHeadExample$Criterion com.jsh.erp.datasource.entities.AccountHeadExample$GeneratedCriteria com.jsh.erp.datasource.entities.AccountHeadExample com.jsh.erp.datasource.entities.AccountHeadVo4ListEx com.jsh.erp.datasource.entities.AccountItem com.jsh.erp.datasource.entities.AccountItemExample$Criteria com.jsh.erp.datasource.entities.AccountItemExample$Criterion com.jsh.erp.datasource.entities.AccountItemExample$GeneratedCriteria com.jsh.erp.datasource.entities.AccountItemExample com.jsh.erp.datasource.entities.AppEntity com.jsh.erp.datasource.entities.AppExample$Criteria com.jsh.erp.datasource.entities.AppExample$Criterion com.jsh.erp.datasource.entities.AppExample$GeneratedCriteria com.jsh.erp.datasource.entities.AppExample com.jsh.erp.datasource.entities.Asset com.jsh.erp.datasource.entities.AssetCategory com.jsh.erp.datasource.entities.AssetCategoryExample$Criteria com.jsh.erp.datasource.entities.AssetCategoryExample$Criterion com.jsh.erp.datasource.entities.AssetCategoryExample$GeneratedCriteria com.jsh.erp.datasource.entities.AssetCategoryExample com.jsh.erp.datasource.entities.AssetExample$Criteria com.jsh.erp.datasource.entities.AssetExample$Criterion com.jsh.erp.datasource.entities.AssetExample$GeneratedCriteria com.jsh.erp.datasource.entities.AssetExample com.jsh.erp.datasource.entities.AssetName com.jsh.erp.datasource.entities.AssetNameExample$Criteria com.jsh.erp.datasource.entities.AssetNameExample$Criterion com.jsh.erp.datasource.entities.AssetNameExample$GeneratedCriteria com.jsh.erp.datasource.entities.AssetNameExample com.jsh.erp.datasource.entities.Depot com.jsh.erp.datasource.entities.DepotExample$Criteria com.jsh.erp.datasource.entities.DepotExample$Criterion com.jsh.erp.datasource.entities.DepotExample$GeneratedCriteria com.jsh.erp.datasource.entities.DepotExample com.jsh.erp.datasource.entities.DepotHead com.jsh.erp.datasource.entities.DepotHeadExample$Criteria com.jsh.erp.datasource.entities.DepotHeadExample$Criterion com.jsh.erp.datasource.entities.DepotHeadExample$GeneratedCriteria com.jsh.erp.datasource.entities.DepotHeadExample com.jsh.erp.datasource.entities.DepotItem com.jsh.erp.datasource.entities.DepotItemExample$Criteria com.jsh.erp.datasource.entities.DepotItemExample$Criterion com.jsh.erp.datasource.entities.DepotItemExample$GeneratedCriteria com.jsh.erp.datasource.entities.DepotItemExample com.jsh.erp.datasource.entities.DepotItemVo4DetailByTypeAndMId com.jsh.erp.datasource.entities.DepotItemVo4HeaderId com.jsh.erp.datasource.entities.DepotItemVo4Material com.jsh.erp.datasource.entities.DepotItemVo4WithInfoEx com.jsh.erp.datasource.entities.Functions com.jsh.erp.datasource.entities.FunctionsExample$Criteria com.jsh.erp.datasource.entities.FunctionsExample$Criterion com.jsh.erp.datasource.entities.FunctionsExample$GeneratedCriteria com.jsh.erp.datasource.entities.FunctionsExample com.jsh.erp.datasource.entities.InOutItem com.jsh.erp.datasource.entities.InOutItemExample$Criteria com.jsh.erp.datasource.entities.InOutItemExample$Criterion com.jsh.erp.datasource.entities.InOutItemExample$GeneratedCriteria com.jsh.erp.datasource.entities.InOutItemExample com.jsh.erp.datasource.entities.Log com.jsh.erp.datasource.entities.LogExample$Criteria com.jsh.erp.datasource.entities.LogExample$Criterion com.jsh.erp.datasource.entities.LogExample$GeneratedCriteria com.jsh.erp.datasource.entities.LogExample com.jsh.erp.datasource.entities.Material com.jsh.erp.datasource.entities.MaterialCategory com.jsh.erp.datasource.entities.MaterialCategoryExample$Criteria com.jsh.erp.datasource.entities.MaterialCategoryExample$Criterion com.jsh.erp.datasource.entities.MaterialCategoryExample$GeneratedCriteria com.jsh.erp.datasource.entities.MaterialCategoryExample com.jsh.erp.datasource.entities.MaterialExample$Criteria com.jsh.erp.datasource.entities.MaterialExample$Criterion com.jsh.erp.datasource.entities.MaterialExample$GeneratedCriteria com.jsh.erp.datasource.entities.MaterialExample com.jsh.erp.datasource.entities.MaterialProperty com.jsh.erp.datasource.entities.MaterialPropertyExample$Criteria com.jsh.erp.datasource.entities.MaterialPropertyExample$Criterion com.jsh.erp.datasource.entities.MaterialPropertyExample$GeneratedCriteria com.jsh.erp.datasource.entities.MaterialPropertyExample com.jsh.erp.datasource.entities.MaterialVo4Unit com.jsh.erp.datasource.entities.Person com.jsh.erp.datasource.entities.PersonExample$Criteria com.jsh.erp.datasource.entities.PersonExample$Criterion com.jsh.erp.datasource.entities.PersonExample$GeneratedCriteria com.jsh.erp.datasource.entities.PersonExample com.jsh.erp.datasource.entities.Role com.jsh.erp.datasource.entities.RoleExample$Criteria com.jsh.erp.datasource.entities.RoleExample$Criterion com.jsh.erp.datasource.entities.RoleExample$GeneratedCriteria com.jsh.erp.datasource.entities.RoleExample com.jsh.erp.datasource.entities.SerialNumber com.jsh.erp.datasource.entities.SerialNumberEx com.jsh.erp.datasource.entities.SerialNumberExample$Criteria com.jsh.erp.datasource.entities.SerialNumberExample$Criterion com.jsh.erp.datasource.entities.SerialNumberExample$GeneratedCriteria com.jsh.erp.datasource.entities.SerialNumberExample com.jsh.erp.datasource.entities.Supplier com.jsh.erp.datasource.entities.SupplierExample$Criteria com.jsh.erp.datasource.entities.SupplierExample$Criterion com.jsh.erp.datasource.entities.SupplierExample$GeneratedCriteria com.jsh.erp.datasource.entities.SupplierExample com.jsh.erp.datasource.entities.SystemConfig com.jsh.erp.datasource.entities.SystemConfigExample$Criteria com.jsh.erp.datasource.entities.SystemConfigExample$Criterion com.jsh.erp.datasource.entities.SystemConfigExample$GeneratedCriteria com.jsh.erp.datasource.entities.SystemConfigExample com.jsh.erp.datasource.entities.Unit com.jsh.erp.datasource.entities.UnitExample$Criteria com.jsh.erp.datasource.entities.UnitExample$Criterion com.jsh.erp.datasource.entities.UnitExample$GeneratedCriteria com.jsh.erp.datasource.entities.UnitExample com.jsh.erp.datasource.entities.User com.jsh.erp.datasource.entities.UserBusiness com.jsh.erp.datasource.entities.UserBusinessExample$Criteria com.jsh.erp.datasource.entities.UserBusinessExample$Criterion com.jsh.erp.datasource.entities.UserBusinessExample$GeneratedCriteria com.jsh.erp.datasource.entities.UserBusinessExample com.jsh.erp.datasource.entities.UserExample$Criteria com.jsh.erp.datasource.entities.UserExample$Criterion com.jsh.erp.datasource.entities.UserExample$GeneratedCriteria com.jsh.erp.datasource.entities.UserExample com.jsh.erp.datasource.mappers.AccountHeadMapper com.jsh.erp.datasource.mappers.AccountItemMapper com.jsh.erp.datasource.mappers.AccountMapper com.jsh.erp.datasource.mappers.AppMapper com.jsh.erp.datasource.mappers.AssetCategoryMapper com.jsh.erp.datasource.mappers.AssetMapper com.jsh.erp.datasource.mappers.AssetNameMapper com.jsh.erp.datasource.mappers.DepotHeadMapper com.jsh.erp.datasource.mappers.DepotHeadMapperEx com.jsh.erp.datasource.mappers.DepotItemMapper com.jsh.erp.datasource.mappers.DepotItemMapperEx com.jsh.erp.datasource.mappers.DepotMapper com.jsh.erp.datasource.mappers.FunctionsMapper com.jsh.erp.datasource.mappers.InOutItemMapper com.jsh.erp.datasource.mappers.LogMapper com.jsh.erp.datasource.mappers.MaterialCategoryMapper com.jsh.erp.datasource.mappers.MaterialMapper com.jsh.erp.datasource.mappers.MaterialMapperEx com.jsh.erp.datasource.mappers.MaterialPropertyMapper com.jsh.erp.datasource.mappers.PersonMapper com.jsh.erp.datasource.mappers.RoleMapper com.jsh.erp.datasource.mappers.SerialNumberMapper com.jsh.erp.datasource.mappers.SerialNumberMapperEx com.jsh.erp.datasource.mappers.SupplierMapper com.jsh.erp.datasource.mappers.SystemConfigMapper com.jsh.erp.datasource.mappers.UnitMapper com.jsh.erp.datasource.mappers.UserBusinessMapper com.jsh.erp.datasource.mappers.UserMapper com.jsh.erp.datasource.vo.AccountItemVo4List com.jsh.erp.datasource.vo.AccountVo4InOutList com.jsh.erp.datasource.vo.AccountVo4List com.jsh.erp.datasource.vo.DepotHeadVo4InDetail com.jsh.erp.datasource.vo.DepotHeadVo4InOutMCount com.jsh.erp.datasource.vo.DepotHeadVo4List com.jsh.erp.datasource.vo.DepotHeadVo4StatementAccount com.jsh.erp.datasource.vo.LogVo4List com.jsh.erp.ErpApplication com.jsh.erp.exception.BusinessParamCheckingException com.jsh.erp.exception.BusinessRunTimeException com.jsh.erp.exception.GlobalExceptionHandler com.jsh.erp.service.account.AccountComponent com.jsh.erp.service.account.AccountResource com.jsh.erp.service.account.AccountService com.jsh.erp.service.accountHead.AccountHeadComponent com.jsh.erp.service.accountHead.AccountHeadResource com.jsh.erp.service.accountHead.AccountHeadService com.jsh.erp.service.accountItem.AccountItemComponent com.jsh.erp.service.accountItem.AccountItemResource com.jsh.erp.service.accountItem.AccountItemService com.jsh.erp.service.app.AppComponent com.jsh.erp.service.app.AppResource com.jsh.erp.service.app.AppService com.jsh.erp.service.CommonQueryManager com.jsh.erp.service.depot.DepotComponent com.jsh.erp.service.depot.DepotResource com.jsh.erp.service.depot.DepotService com.jsh.erp.service.depotHead.DepotHeadComponent com.jsh.erp.service.depotHead.DepotHeadResource com.jsh.erp.service.depotHead.DepotHeadService com.jsh.erp.service.depotItem.DepotItemComponent com.jsh.erp.service.depotItem.DepotItemResource com.jsh.erp.service.depotItem.DepotItemService com.jsh.erp.service.functions.FunctionsComponent com.jsh.erp.service.functions.FunctionsResource com.jsh.erp.service.functions.FunctionsService com.jsh.erp.service.ICommonQuery com.jsh.erp.service.inOutItem.InOutItemComponent com.jsh.erp.service.inOutItem.InOutItemResource com.jsh.erp.service.inOutItem.InOutItemService com.jsh.erp.service.InterfaceContainer com.jsh.erp.service.log.LogComponent com.jsh.erp.service.log.LogResource com.jsh.erp.service.log.LogService com.jsh.erp.service.material.MaterialComponent com.jsh.erp.service.material.MaterialResource com.jsh.erp.service.material.MaterialService com.jsh.erp.service.materialCategory.MaterialCategoryComponent com.jsh.erp.service.materialCategory.MaterialCategoryResource com.jsh.erp.service.materialCategory.MaterialCategoryService com.jsh.erp.service.materialProperty.MaterialPropertyComponent com.jsh.erp.service.materialProperty.MaterialPropertyResource com.jsh.erp.service.materialProperty.MaterialPropertyService com.jsh.erp.service.person.PersonComponent com.jsh.erp.service.person.PersonResource com.jsh.erp.service.person.PersonService com.jsh.erp.service.ResourceInfo com.jsh.erp.service.role.RoleComponent com.jsh.erp.service.role.RoleResource com.jsh.erp.service.role.RoleService com.jsh.erp.service.serialNumber.SerialNumberComponent com.jsh.erp.service.serialNumber.SerialNumberResource com.jsh.erp.service.serialNumber.SerialNumberService com.jsh.erp.service.supplier.SupplierComponent com.jsh.erp.service.supplier.SupplierResource com.jsh.erp.service.supplier.SupplierService com.jsh.erp.service.systemConfig.SystemConfigComponent com.jsh.erp.service.systemConfig.SystemConfigResource com.jsh.erp.service.systemConfig.SystemConfigService com.jsh.erp.service.unit.UnitComponent com.jsh.erp.service.unit.UnitResource com.jsh.erp.service.unit.UnitService com.jsh.erp.service.user.UserComponent com.jsh.erp.service.user.UserResource com.jsh.erp.service.user.UserService com.jsh.erp.service.userBusiness.UserBusinessComponent com.jsh.erp.service.userBusiness.UserBusinessResource com.jsh.erp.service.userBusiness.UserBusinessService com.jsh.erp.utils.AnnotationUtils com.jsh.erp.utils.BaseResponseInfo com.jsh.erp.utils.ColumnPropertyUtil com.jsh.erp.utils.Constants com.jsh.erp.utils.ErpInfo com.jsh.erp.utils.ExcelUtils com.jsh.erp.utils.ExceptionCodeConstants$UserExceptionCode com.jsh.erp.utils.ExceptionCodeConstants com.jsh.erp.utils.ExportExecUtil com.jsh.erp.utils.ExtJsonUtils$1 com.jsh.erp.utils.ExtJsonUtils$ExtExtractor com.jsh.erp.utils.ExtJsonUtils$ExtFilter com.jsh.erp.utils.ExtJsonUtils$MetaData com.jsh.erp.utils.ExtJsonUtils$NPDoubleSerializer com.jsh.erp.utils.ExtJsonUtils$NPFloatCodec com.jsh.erp.utils.ExtJsonUtils com.jsh.erp.utils.FileUtils com.jsh.erp.utils.JshException com.jsh.erp.utils.JsonUtils com.jsh.erp.utils.OrderUtils com.jsh.erp.utils.PageQueryInfo com.jsh.erp.utils.ParamUtils com.jsh.erp.utils.QueryUtils com.jsh.erp.utils.RegExpTools$RegExp com.jsh.erp.utils.RegExpTools com.jsh.erp.utils.ResponseCode com.jsh.erp.utils.ResponseJsonUtil$ResponseFilter com.jsh.erp.utils.ResponseJsonUtil com.jsh.erp.utils.StringUtil com.jsh.erp.utils.Tools ";
}
