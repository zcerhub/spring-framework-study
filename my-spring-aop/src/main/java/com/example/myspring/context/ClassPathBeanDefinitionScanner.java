package com.example.myspring.context;

import com.example.myspring.beans.BeanDefinitionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;


public class ClassPathBeanDefinitionScanner {

    private static Logger logger = LoggerFactory.getLogger(ClassPathBeanDefinitionScanner.class);

    private BeanDefinitionRegistry registry;

    private BeanDefinitionReader reader;

    private PathMatcher pathMatcher = new AntPathMatcher();

    private String resourcePatter = "**/*.class";

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
        this.reader = new AnnotationBeanDefintionReader(this.registry);
    }

    public void scan(String... basePackages) {
        if (basePackages != null && basePackages.length > 0) {
            for (String p : basePackages) {
                this.reader.loadBeanDefinitions(this.doScan(p));
            }
        }
    }

    private Resource[] doScan(String basePackage) {
//        扫描包下的类
//        构造初步配置模式串，= 给如的包串 + / + **/*.class，替换里面的.为/
        String pathPattern = StringUtils.replace(basePackage, ".", "/") + "/" + this.resourcePatter;
        if (pathPattern.charAt(0) != '/') {
            pathPattern = "/" + pathPattern;
        }
//        找出模式的根包路径
        String rootPath = this.determineRootDir(pathPattern);
//        得到的文件名匹配的绝对路径模式
        String fullPattern = this.getClass().getResource("/").toString() + pathPattern;
//        根据根包理解得到根包对应的目录
        File rootDir = new File(this.getClass().getResource(rootPath).toString());
//        存放找到的类文件的resource集合
        Set<Resource> scanedClassFileResources = new HashSet<>();
//        调用doRetrieveMatchingFiles来扫描class文件
        this.doRetrieveMatchingFile(fullPattern, rootDir, scanedClassFileResources);
        return (Resource[]) scanedClassFileResources.toArray();
    }

    /*
    * 递归找指定目力下的所有类，匹配模式的加入到结果中
    * */
    private void doRetrieveMatchingFile(String fullPattern, File dir, Set<Resource> result) {
        if (logger.isTraceEnabled()) {
            logger.trace("search directory ["+dir.getAbsolutePath()+"] for files matching pattern ["+fullPattern+"]");
        }
        for (File content : listDirectory(dir)) {
            String currPath = StringUtils.replace(content.getAbsolutePath(), File.separator, "/");
            if (content.isDirectory() && getPathMatcher().matchStart(fullPattern, currPath + "/")) {
                if (!content.canRead()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("skipping subdirectory ["+dir.getAbsolutePath()+"] because the application is not allowed" +
                                "to read the directory");
                    }
                }else{
                    doRetrieveMatchingFile(fullPattern,content,result);
                }
            }
            if (getPathMatcher().match(fullPattern, currPath)) {
                result.add(new FileSystemResource(content));
            }
        }
    }

    protected File[] listDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files == null) {
            return new File[0];
        }
        Arrays.sort(files, Comparator.comparing(File::getName));
        return files;
    }

    private PathMatcher getPathMatcher() {
        return pathMatcher;
    }

    private String determineRootDir(String location) {
        int rootDirEnd = location.length();
        rootDirEnd = location.indexOf("*");
        int zi = location.indexOf("?");
        if (zi != -1 && zi < rootDirEnd) {
            rootDirEnd = location.lastIndexOf("/", zi);
        }
        if (rootDirEnd != -1) {
            return location.substring(0, rootDirEnd);
        }else{
            return location;
        }
    }

}
