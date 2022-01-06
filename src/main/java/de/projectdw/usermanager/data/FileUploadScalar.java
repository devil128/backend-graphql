package de.projectdw.usermanager.data;

import com.netflix.graphql.dgs.DgsScalar;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

import javax.servlet.http.Part;
import java.io.IOException;

@DgsScalar(name="FileUpload")
public class FileUploadScalar implements Coercing<FileUpload, Void> {
    @Override
    public Void serialize(Object dataFetcherResult) {
        throw new CoercingSerializeException("Upload is an input-only type");
    }

    @Override
    public FileUpload parseValue(Object input) {
        if (input instanceof Part) {
            Part part = (Part) input;
            try {
                String contentType = part.getContentType();
                byte[] content = new byte[part.getInputStream().available()];
                part.delete();
                return new FileUpload(contentType, content);

            } catch (IOException e) {
                throw new CoercingParseValueException("Couldn't read content of the uploaded file");
            }
        } else if (null == input) {
            return null;
        } else {
            throw new CoercingParseValueException(
                    "Expected type " + Part.class.getName() + " but was " + input.getClass().getName());
        }
    }

    @Override
    public FileUpload parseLiteral(Object input) {
        throw new CoercingParseLiteralException(
                "Must use variables to specify Upload values");
    }

}

