/*******************************************************************************
 * Copyright 2018 Maximilian Stark | Dakror <mail@dakror.de>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.dakror.quarry.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.FitViewport;

import de.dakror.common.libgdx.PlatformInterface;
import de.dakror.common.libgdx.ui.Scene;
import de.dakror.quarry.Const;
import de.dakror.quarry.Quarry;
import net.spookygames.gdx.sfx.SfxMusic;
import net.spookygames.gdx.sfx.SfxMusicLoader;
import net.spookygames.gdx.sfx.SfxSound;
import net.spookygames.gdx.sfx.SfxSoundLoader;

/**
 * @author Maximilian Stark | Dakror
 */
public class LoadingScreen extends Scene {
    long l;

    Label label;

    TextureRegion progress;
    TextureRegion bg;

    float prog;
    float visualProg;
    float oldVisualProg;
    float interp;

    boolean finishingUp = false;

    @Override
    public void init() {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        AssetManager assets = new AssetManager(resolver);
        Quarry.Q.assets = assets;
        assets.setLoader(SfxMusic.class, new SfxMusicLoader(resolver));
        assets.setLoader(SfxSound.class, new SfxSoundLoader(resolver));

        assets.load("tex.atlas", TextureAtlas.class);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Medium.ttf"));
        ObjectMap<String, Object> fontMap = new ObjectMap<String, Object>();
        fontMap.put("small-font", createFont(generator, 24));
        fontMap.put("default-font", createFont(generator, 32));
        fontMap.put("big-font", createFont(generator, 48));
        SkinParameter param = new SkinParameter("tex.atlas", fontMap);

        assets.load("skin.json", Skin.class, param);

        assets.finishLoading();

        Quarry.Q.atlas = assets.get("tex.atlas");


        Quarry.Q.skin = assets.get("skin.json");
        Quarry.Q.font = Quarry.Q.skin.getFont("default-font");

        Quarry.Q.font.getData().markupEnabled = true;
        Quarry.Q.font.setFixedWidthGlyphs("0123456789-+");
        Quarry.Q.skin.getFont("small-font").setFixedWidthGlyphs("0123456789");

        progress = Quarry.Q.atlas.findRegion("structure_conveyor_we");
        bg = Quarry.Q.atlas.findRegion("button");
        Quarry.Q.mouseTex = Quarry.Q.atlas.findRegion("mouse");

        ///////////////////////////

        stage = new Stage(new FitViewport(Const.UI_W, Const.UI_H));
        stage.setActionsRequestRendering(false);
        label = new Label(Quarry.Q.i18n.get("loading.sounds"), Quarry.Q.skin);
        label.setAlignment(Align.center);
        Table t = new Table();
        t.setBackground(Quarry.Q.skin.getTiledDrawable("tile_stone"));
        t.add(label).grow();
        t.setSize(Const.UI_W, Const.UI_H);
        stage.addActor(t);

        ///////////////////////////

        assets.load("sfx/airpurifier" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/anchorportal" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/arcwelder" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/assembler" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/ballmill" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/bender" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/boiler" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/booster" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/carpenter" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/centrifuge" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/charcoalmound" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/column" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/compactor" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/condenser" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/crucible" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/devicefabricator" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/digitalstorage" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/drawer" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/excavator" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/furnace" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/injection" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/kiln" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/mason" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/mine" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/mixer" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/node" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/oilwell" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/polarizer" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/polymerizer" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/pump1" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/refinery" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/rockcrusher" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/rollingmachine" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/sawmill" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/science" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/shaftdrill" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/stacker" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/turbine" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/waterwheel" + Const.SFX_FORMAT, SfxSound.class);
        assets.load("sfx/woodcutter" + Const.SFX_FORMAT, SfxSound.class);

        assets.load("sfx/click3" + Const.SFX_FORMAT, Sound.class);
        assets.load("sfx/build" + Const.SFX_FORMAT, Sound.class);
        assets.load("sfx/destroy" + Const.SFX_FORMAT, Sound.class);
        assets.load("sfx/cable" + Const.SFX_FORMAT, Sound.class);

        assets.load("sfx/ambience_empty" + Const.SFX_FORMAT, Sound.class);
        assets.load("sfx/ambience_base" + Const.SFX_FORMAT, Sound.class);
        assets.load("sfx/ambience_heavy" + Const.SFX_FORMAT, Sound.class);

        assets.load("music/Fading_into_the_Dream" + Const.MUSIC_FORMAT, Music.class);
        assets.load("music/Impact Prelude" + Const.MUSIC_FORMAT, Music.class);

        l = System.currentTimeMillis();
    }

    protected BitmapFont createFont(FreeTypeFontGenerator gen, float dp) {
        FreeTypeFontParameter param = new FreeTypeFontParameter();
        param.magFilter = TextureFilter.Linear;
        param.minFilter = TextureFilter.Linear;
        param.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "你缺乏一些科学知识来描绘这个蓝图。此自更新的游戏版本，无法加载该存档由完整创建在演示中请重启使语言改生效文件区域网格增强筑造电缆切断选择拆除用器：共享时间并且同步粘贴暂停从库取出将退还资源放入储打开技目录产摘要燃料元满托盘液体油材原矿粉可堆叠消否确定是删吗？不撤销恭喜已经成了采石场全部内容希望欢前往商店购买支持小型独立发者工作我会非常感激！论都结束保所以卡里如果现它继续玩栋其有物品永远失相连漏斗起钻头覆盖先当城高级机架池砖块青铜锭板丝碳纤维水泥木炭（）芯片土煤混凝碎管桶细炸药空罐天然气玻璃金火硬化钢属预处理蒸馏铁铅润滑剂磁熔融硅银锡钛纸张塑珠壳合压汽黄英精炼转子沙脚手隧道井段线硫磺晶涡轮正声音导读外夹回主菜单没另为教程输名种码意味着什么就按运或缩地基础升通过上写东西助推换附近速度很己组装巅峰之至少计算谨慎施心最终收多力供应待足大功率复杂获得被极而性制折弯和扭曲却够壮试扩械挖掘深提进术始动价值黑抽利泵集五颜六色实错但形状行滤解锁筛及航太阳能表量让坚圆长纯净必须把命变业优势等周围尘埃锚充低口配弧焊构接防耐汇编盐良好护套封底面填包裹根棒标准尺寸叶到杆固几台球磨较软数某式质炉任何类沸烧需温越受流热竖两邻层下对矩阵离分含见稀快致密粗巨冷传送带沿方向桥允许条互交叉每秒千焦均匀布坩埚拉设备模具字据耗即柱萃顶缓慢后引爆灌便于限双循环串联只边置冶铸倒注态降梯窑陶伐人植砍树匠割漂亮剩余浆与浸泡洗才仅侧聚培养链塔户厂普岩辊平锯验室摆弄看奇情况彼筒仓光核依次垛卸比再们去节点适冲超泄箱简绕轴真阀门控车洒项勤剪败拒绝访问活迎骤击息查第止世界诸达埋藏宝样特征直他“”键辅拖拽端志轨哎呀像题旋钮…误事返随指移视野屏幕滚箭鼠仔观察称初显左角右总首稳规、述求脉绿调位恢尽考虑报告反馈未胜谢您截翻修默认关闭减统帝盒际别鸣谈汉";
        if (Quarry.Q.desktop) {
            //param.size = (int) (dp * (float) Gdx.graphics.getDensity()); // for 4K screen
            param.size = (int) (dp * (float) Quarry.Q.pi.message(Const.MSG_DPI, null));
        } else {
            param.size = (int) (dp * (float) Quarry.Q.pi.message(Const.MSG_DPI, null));
        }

        BitmapFont font = gen.generateFont(param);
        font.getData().markupEnabled = true;
        return font;
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);

        float p = Quarry.Q.assets.getProgress();

        if (p != prog) {
            oldVisualProg = visualProg;
            interp = 0;
            prog = p;
        }

        if (visualProg != prog) {
            if (interp >= 1) {
                visualProg = prog;
                interp = 0;
            } else {
                visualProg = oldVisualProg + (prog - oldVisualProg) * interp;
                interp += deltaTime / 0.1f;
            }
        }

        if (Quarry.Q.assets.getProgress() > 0.9f) {
            label.setText(Quarry.Q.i18n.get("loading.buildings"));
        }

        try {
            if (!finishingUp && Quarry.Q.assets.update() && visualProg == 1 && !fadeOut) {
                System.out.println("Asset loading took " + (System.currentTimeMillis() - l) + " ms");
                finishingUp = true;
                Quarry.Q.loadingFinished();
            }
        } catch (Exception e) {
            Quarry.Q.pi.message(PlatformInterface.MSG_EXCEPTION, e);
        }
    }

    @Override
    public void draw() {
        Gdx.gl.glClearColor(127 / 256f, 127 / 256f, 127 / 256f, 1);
        super.draw();

        stage.getBatch().begin();

        stage.getBatch().setColor(0, 0, 0, 0.5f);

        int x = (int) ((Const.UI_W - 600) / 2);
        stage.getBatch().draw(bg, x, Const.UI_H / 4, 600, 40);
        stage.getBatch().setColor(1, 1, 1, 1);

        float p = 600 * visualProg;
        for (int i = 0; i < (int) (p) / 64; i++)
            stage.getBatch().draw(progress, x + i * 64, Const.UI_H / 4 - 12);

        float len = (int) (p / 64) * 64;
        stage.getBatch().draw(progress.getTexture(), x + len, Const.UI_H / 4 - 12, p - len, 64, progress.getU(), progress.getV2(), (progress.getU2() - progress.getU()) * (p - len) / 64 + progress.getU(), progress.getV());
        stage.getBatch().end();
    }
}
