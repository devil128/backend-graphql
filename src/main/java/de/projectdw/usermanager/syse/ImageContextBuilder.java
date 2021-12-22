package de.projectdw.usermanager.syse;

import com.netflix.graphql.dgs.context.DgsCustomContextBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImageContextBuilder implements DgsCustomContextBuilder<ImageContext> {

    private List<Image> imageList;

    public ImageContextBuilder withProjects(List<Image> images) {
        this.imageList = images;
        return this;
    }

    @Override
    public ImageContext build() {
        return new ImageContext(imageList);
    }
}
