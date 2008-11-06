package org.genedb.web.gui;

import org.genedb.util.MD5Util;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

/**
 * Create a class responsible for the physical dumping of an image to a file,
 * and all configuration for it's location and URL
 *
 * @author art
 */
public class FileDiagramCache implements DiagramCache {
    private static final Logger logger = Logger.getLogger(FileDiagramCache.class);


    private String baseDiagramDirectory;
    private String baseUri = "";

    /* (non-Javadoc)
     * @see org.genedb.web.gui.DiagramCache#getBaseUri()
     */
    public String getBaseUri() {
        return baseUri;
    }



    private String contextMapRenderDirectory;
    private String proteinMapRenderDirectory;


    private File contextMapRootDir;


    private File proteinMapRootDir;


    @PostConstruct
    private void initDirs() {
        logger.debug("In initDirs for diagramcache");
        File baseDir = new File(baseDiagramDirectory);
        if (!baseDir.exists() || !baseDir.canWrite()) {
            throw new RuntimeException(String.format("The diagram cache '%s' doesn't exist or isn't writable", baseDiagramDirectory));
        }

        contextMapRootDir = new File(baseDir, contextMapRenderDirectory);
        if (!contextMapRootDir.exists()) {
            contextMapRootDir.mkdirs();
        }

        proteinMapRootDir = new File(baseDir, proteinMapRenderDirectory);
        if (!proteinMapRootDir.exists()) {
            proteinMapRootDir.mkdirs();
        }
    }



    /* (non-Javadoc)
     * @see org.genedb.web.gui.DiagramCache#fileForContextMap(org.genedb.web.gui.RenderedContextMap)
     */
    public String fileForContextMap(RenderedContextMap renderedContextMap) throws IOException {

        ContextMapDiagram diagram = renderedContextMap.getDiagram();

        File organismDir = new File(contextMapRootDir, diagram.getOrganism());
        if (!organismDir.exists()) {
            logger.warn(String
                    .format("Directory '%s' not found; attempting to create", organismDir));
            boolean success = organismDir.mkdir();
            if (!success) {
                throw new IOException("Failed to create directory: " + organismDir);
            }
        }

        String cacheFileName = fileForDiagram(renderedContextMap, organismDir);;

        String path = cacheFileName.substring(baseDiagramDirectory.length());

        return baseUri + '/' + path;
    }


    /* (non-Javadoc)
     * @see org.genedb.web.gui.DiagramCache#fileForProteinMap(org.genedb.web.gui.RenderedProteinMap)
     */
    public String fileForProteinMap(RenderedProteinMap renderedProteinMap) throws IOException {

        Assert.notNull(proteinMapRootDir);

        ProteinMapDiagram diagram = renderedProteinMap.getDiagram();

        File organismDir = new File(proteinMapRootDir, diagram.getOrganism());
        if (!organismDir.exists()) {
            logger.warn(String
                    .format("Directory '%s' not found; attempting to create", organismDir));
            boolean success = organismDir.mkdir();
            if (!success) {
                throw new IOException("Failed to create directory: " + organismDir);
            }
        }

        String cacheFileName = fileForDiagram(renderedProteinMap, organismDir);

        return baseUri + cacheFileName.substring(baseDiagramDirectory.length());
    }


    private String fileForDiagram(RenderedDiagram renderedDiagram, File chromosomeDir)
        throws IOException {

        String lastPartFileName = renderedDiagram.getPreferredFilename();

        String cacheFileName = chromosomeDir.getCanonicalPath() + '/' + MD5Util.getPathBasedOnMD5(lastPartFileName, '/');

        File cacheFile = new File(cacheFileName);
        cacheFile.getParentFile().mkdirs();

        File tempFile  = new File(cacheFileName + ".new");
        File lockFile  = new File(cacheFileName + ".lock");

        /*
         * Currently we ALWAYS create the file, even if it already exists. The reason
         * is that, if the diagram has not been rendered, the <code>renderedFeatures</code>
         * collection is not populated, which is needed in the model. This is a somewhat
         * unsatisfactory situation, which may be reconsidered in future.
         */
//        if (cacheFile.exists()) {
//            return cacheFileRelativePath;
//        }

        FileOutputStream tempOutputStream = new FileOutputStream(tempFile);
        FileOutputStream lockOutputStream = new FileOutputStream(lockFile);
        lockOutputStream.getChannel().lock();
        try {
            renderedDiagram.writeTo(tempOutputStream);
        }
        catch (Exception exp) {
            tempOutputStream.close();
            tempFile.delete();
            lockOutputStream.close();
            lockFile.delete();

            throw new RuntimeException(
                String.format("Failed to create image '%s'", cacheFile), exp);
        }

        tempOutputStream.close();
        if (!tempFile.renameTo(cacheFile)) {
            throw new RuntimeException(String.format("Failed to rename '%s' to '%s'", tempFile, cacheFile));
        }

        lockOutputStream.close(); // Releases lock
        lockFile.delete();

        assert cacheFile.exists();
        return cacheFileName;
    }

    @Required
    public void setBaseDiagramDirectory(String baseDiagramDirectory) {
        this.baseDiagramDirectory = baseDiagramDirectory;
    }

    @Required
    public void setContextMapRenderDirectory(String contextMapRenderDirectory) {
        this.contextMapRenderDirectory = contextMapRenderDirectory;
    }

    @Required
    public void setProteinMapRenderDirectory(String proteinMapRenderDirectory) {
        this.proteinMapRenderDirectory = proteinMapRenderDirectory;
    }

    @Required
    public void setBaseUri(String baseUri) {
        logger.trace("Setting baseUri to "+ baseUri);
        this.baseUri = baseUri;
    }


    public File getContextMapRootDir() {
        return contextMapRootDir;
    }

}
