package MsGraph.Java.DeviceCode;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.microsoft.aad.msal4j.ITokenCacheAccessAspect;
import com.microsoft.aad.msal4j.ITokenCacheAccessContext;

public class TokenCache implements ITokenCacheAccessAspect{

    private Path filePath;
    public TokenCache(String filename) throws Exception{
        Path cachePath = Paths.get(System.getProperty("user.home"), filename);
        filePath = cachePath;
        File cacheFile = new File(cachePath.toUri());

        if(!cacheFile.exists()){
            cacheFile.createNewFile();
        }
    }

    @Override
    public void beforeCacheAccess(ITokenCacheAccessContext iTokenCacheAccessContext) {
        // Read data from disk. Consider decrypting this data it was encrypted.
        try{
            String data = new String(Files.readAllBytes(filePath));
            iTokenCacheAccessContext.tokenCache().deserialize(data);
        } catch (Exception ex){
            System.out.println("Error reading data from file: "+ ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void afterCacheAccess(ITokenCacheAccessContext iTokenCacheAccessContext) {
        // Write data to disk. Consider encrypting this data.
        try{
            String data = iTokenCacheAccessContext.tokenCache().serialize();
            Files.write(filePath, data.getBytes());
        } catch (Exception ex){
            System.out.println("Error writing data to file: "+ ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
