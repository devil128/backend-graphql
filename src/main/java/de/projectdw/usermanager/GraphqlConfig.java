package de.projectdw.usermanager;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsRuntimeWiring;
import de.projectdw.usermanager.syse.FileUpload;
import graphql.schema.*;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.Part;
import java.io.IOException;

@DgsComponent
public class GraphqlConfig {


    public static final GraphQLScalarType FileUpload = new GraphQLScalarType(
            "FileUpload",
            "A file part in a multipart request",
            new Coercing<FileUpload, Void>() {

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
            });
}
