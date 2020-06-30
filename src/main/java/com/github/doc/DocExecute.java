package com.github.doc;

import com.github.doc.model.ClassDoc;
import com.github.doc.model.merge.MergeInterfaceDoc;
import com.github.doc.parse.ClassParser;
import com.github.doc.parse.EnumParser;
import com.github.doc.parse.InterfaceParser;
import com.github.doc.parse.Parser;
import com.github.doc.tool.InterfaceMerge;
import com.github.doc.util.CollectionUtils;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wangdongbo
 * @since 2020/3/19.
 */
public class DocExecute {

    private static final List<Parser> PARSERS = Lists.newArrayList();

    static {
        PARSERS.add(new ClassParser());
        PARSERS.add(new InterfaceParser());
        PARSERS.add(new EnumParser());
    }

    public static <R extends ClassDoc> List<R> generateDoc(List<String> classFilePaths) throws FileNotFoundException {
        if (CollectionUtils.isEmpty(classFilePaths)) {
            return Collections.emptyList();
        }
        List<R> classDocs = Lists.newArrayList();
        for (String classFilePath : classFilePaths) {
            File file = new File(classFilePath);
            if (!file.exists() || !file.isFile()) {
                continue;
            }
            ParseResult<CompilationUnit> unit = new JavaParser().parse(new File(classFilePath));
            if (!unit.getResult().isPresent()) {
                continue;
            }
            if (CollectionUtils.isEmpty(unit.getResult().get().getTypes())) {
                continue;
            }
            TypeDeclaration typeDeclaration = unit.getResult().get().getType(0);
            for (Parser parser : PARSERS) {
                R r = parser.parse(typeDeclaration);
                if (r != null) {
                    r.setClassFilePath(classFilePath);
                    classDocs.add(r);
                    break;
                }
            }
        }
        return classDocs;
    }

    @SuppressWarnings("unchecked")
    public static <R extends ClassDoc> List<R> generateDoc(List<String> fileOrDirs, List<String> filterClass, boolean recursive) throws FileNotFoundException {
        if (CollectionUtils.isEmpty(fileOrDirs)) {
            return Collections.emptyList();
        }
        Set<String> filters = Collections.emptySet();
        if (CollectionUtils.isNotEmpty(filterClass)) {
            filters = Sets.newHashSet(filterClass);
        }
        List<String> paths = Lists.newArrayList();
        for (String dir : fileOrDirs) {
            File file = new File(dir);
            if (!file.exists()) {
                continue;
            }
            if (file.isFile() && !filters.contains(file.getAbsolutePath())) {
                paths.add(file.getAbsolutePath());
                continue;
            }
            if (file.isDirectory()) {
                Collection<File> collection = FileUtils.listFiles(file, null, recursive);
                if (CollectionUtils.isNotEmpty(collection)) {
                    Set<String> finalFilters = filters;
                    collection.stream().map(File::getAbsolutePath).filter(s -> !finalFilters.contains(s)).forEach(paths::add);
                }
            }
        }
        return generateDoc(paths);
    }

    public static <R extends ClassDoc> List<R> generateDoc(Map<String, String> fileContentMap) throws IOException {
        if (CollectionUtils.isEmpty(fileContentMap)) {
            return Collections.emptyList();
        }
        List<R> classDocs = Lists.newArrayList();
        for (Map.Entry<String, String> entry : fileContentMap.entrySet()) {
            ParseResult<CompilationUnit> unit = new JavaParser().parse(IOUtils.toInputStream(entry.getValue(), "utf-8"));
            if (!unit.getResult().isPresent()) {
                continue;
            }
            if (CollectionUtils.isEmpty(unit.getResult().get().getTypes())) {
                continue;
            }
            TypeDeclaration typeDeclaration = unit.getResult().get().getType(0);
            for (Parser parser : PARSERS) {
                R r = parser.parse(typeDeclaration);
                if (r != null) {
                    r.setClassFilePath(entry.getKey());
                    classDocs.add(r);
                    break;
                }
            }
        }
        return classDocs;
    }

    public static List<MergeInterfaceDoc> generateMergeInterfaceDoc(List<String> classFilePaths) throws FileNotFoundException {
        return InterfaceMerge.merge(generateDoc(classFilePaths));
    }

    public static List<MergeInterfaceDoc> generateMergeInterfaceDoc(List<String> fileOrDirs, List<String> filterClass, boolean recursive) throws FileNotFoundException {
        return InterfaceMerge.merge(generateDoc(fileOrDirs, filterClass, recursive));
    }

    public static List<MergeInterfaceDoc> generateMergeInterfaceDoc(Map<String, String> fileContentMap) throws IOException {
        return InterfaceMerge.merge(generateDoc(fileContentMap));
    }

}
