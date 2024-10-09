package cn.mcmod.effect_export;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

public class ImgHelper {
    private final String modid;
    private Map<String, String> langMap = new HashMap<>();
    private final List<Data> dataList = new ArrayList<>();

    public ImgHelper(@Nonnull String modid) throws IOException {
        this.modid = modid;
        JsonHandler json = new JsonHandler();
        json.langHelper();
        getEffect();
        json.init();
    }

    private void getEffect() throws IOException {
        for (MobEffect effect : BuiltInRegistries.MOB_EFFECT) {
            var effectName = BuiltInRegistries.MOB_EFFECT.getKey(effect);
            assert effectName != null;
            if (!effectName.getNamespace().equals(modid)) continue;
            String name = effect.getDisplayName().getString();
            String englishname = langMap.getOrDefault(effect.getDescriptionId(), effect.getDisplayName().getString());
            String small = getIcon(32, effectName);
            String large = getIcon(128, effectName);

            dataList.add(new Data(effectName.toString(), name, englishname, small, large));
        }
    }

    private String getIcon(int size, ResourceLocation location) throws IOException {
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("assets/" + modid + "/textures/mob_effect/" + location.getPath() + ".png");
        assert input != null;
        BufferedImage image = ImageIO.read(input);
        input.close();

        return image != null
                ? Base64.getEncoder().encodeToString(getByteByImg(resizeImage(image, size)))
                : "null";
    }

    private byte[] getByteByImg(BufferedImage image) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(image, "png", output);
        byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }

    private BufferedImage resizeImage(BufferedImage img, int size) {
        int imgSize = 2;
        if (size == 128) imgSize = 8;

        BufferedImage newImg = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        newImg.getGraphics().drawImage(img.getScaledInstance(size, size, Image.SCALE_SMOOTH), 0, 0, null);
        return newImg;
    }



    private class JsonHandler {
        private final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        public void init() throws IOException {
            File file = new File(System.getProperty("user.dir") + File.separator + "export", modid + "_effect.json");
            if (!file.getParentFile().isDirectory()) file.getParentFile().mkdirs();
            if (!file.isFile() || !file.exists()) file.createNewFile();

            FileOutputStream stream = new FileOutputStream(file);
            stream.write(gson.toJson(dataList).getBytes(StandardCharsets.UTF_8));
            stream.close();
        }

        public void langHelper() throws IOException {
            InputStream input = this.getClass().getClassLoader().getResourceAsStream("assets/" + modid + "/lang/en_us.json");
            if (input == null) {
                langMap = Maps.newHashMap();
                return;
            }

            byte[] text = input.readAllBytes();
            input.close();
            langMap = gson.fromJson(new String(text, StandardCharsets.UTF_8), new TypeToken<Map<String, String>>() {}.getType());
        }
    }
}
