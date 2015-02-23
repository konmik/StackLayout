package info.android15.stacklayoutexample;

public class Model {
    public static class Note {
        public int id;
        public String text;

        public Note(int id, String text) {
            this.id = id;
            this.text = text;
        }

        @Override
        public String toString() {
            return "" + id + " " + text;
        }
    }

    private static int currentSet = 0;

    public static void setCurrentSet(int array) {
        currentSet = array;
    }

    public static Note[] getCurrentSet() {
        return currentSet == 0 ? NOTES : NOTES2;
    }

    public static final Note[] NOTES = {
        new Note(1, "Ut quem persius quaeque has. Cu qui splendide consequat deterruisset, vidit nostrum ei qui. Vis ex odio mazim, et eos malorum labitur, no pertinacia definitionem sed. Has dicit consetetur quaerendum id, eu alii alia legendos his."),
        new Note(2, "Usu at sumo disputationi, qui assum putant dolorum id, quo feugait accusamus hendrerit ei. Luptatum accommodare nam at. Mei putent graecis vivendum ad, enim modus mundi mea an, eos laudem cetero ad. Vel accusam corrumpit urbanitas ne, ipsum voluptatibus ei vis. Nec ut cibo convenire, mea quas munere expetenda ne. Mea ei altera virtute legimus, lorem adipiscing dissentias an duo."),
        new Note(3, "Usu vocent luptatum ad, dicam aliquando et vel, assum conceptam eam no. Cu cum assum menandri. Pri deleniti vivendum in, dicta antiopam mel ut. Mea in alii simul reformidans. Et sed posse aeque ocurreret, diam referrentur an eos, mea ad dolorum alienum. Accusam facilisi theophrastus ei est, ne tale lorem mediocritatem pri, qui dolor omnesque et."),
        new Note(4, "No ubique laoreet maluisset usu. Eos et ignota reprehendunt, eu cum illum tollit insolens, pro aperiri docendi dissentiunt no. Ut per probatus argumentum comprehensam, cu pri vidisse probatus tincidunt. No mei blandit eligendi liberavisse. Eam ex congue legendos reformidans, sed an solum error dolore, id mea paulo labore."),
        new Note(5, "Sit tantas labitur eripuit ex. Eam enim facilisi id, vel te magna elitr hendrerit. Ius ei vero fierent contentiones. Duo et luptatum persequeris, id suas erroribus sed, errem omittam eu eam.")
    };

    public static final Note[] NOTES2 = {
        new Note(1, "意スた立7問べ外34表タ通合オネ間室ネセ一治害ミシチ急芸身シ宿郭ヤコヒヘ増曲ごよみ許住ラチカ導打もんのみ撮手ソエ兵悲びくて。業ヨネウモ慶国のでゃり券空てうで況歌関フセ地名ほリ紙言べばあ期法エホ数明った独日及ケマ応情やとをえ送番ヒ金逃明了終みか。恵市ぎびす優購トこま際世全けラ復図ち府3会ンちゆも棋年鍵カ達2変リ多8旅月町王媛緒れ。"),
        new Note(2, "給モヨ働夜作ソサ覚住再ふ岡見を気姿くえねべ知木テレフ堂支89作ずもづし中球ネ始賃た界月くっよ航心偶凝峡よッ。目フべまよ置年うるを眼完リ映名たど行一社フ進間ヨハネ介信ヘホシク出62教9学むてのわ聞6供示27明コヤツカ快単得のほ。地接エソ無内コタユメ映受マイチ貧都うまらも客工にドぴ界公みどそで表問ゃンえ広誠みざ仙読てみじこ分秋面む状条企ワテス輔者ゆはばか備逆リ府案イオニレ北全怒虐協なろやば。"),
        new Note(3, "1難れ意式リごさ会50暮カ辺2名線キトイミ景雪留約ぶりげど月学オ位高ッおつ営外ンだは立向メヲ損東申高畑らせやり。話ざ前記表ほくび催立でげ清狙メルコノ新終ウミ氏陥難ツ現突そルっー決忍へづけざ米速り輪誘ヱシキツ店海至ルぐフイ真拘肌陶作せちれ。野メナ解的ゆ議購モエノ文掲わ会組ラヒヌ意埼サレアニ矛6元京たへス用全モクロヘ年思せつ秘台イむとせ申業人16駆ルフホ全険いせ幹会ワミレタ公余ラほ。"),
        new Note(4, "夫ニレルシ民拡由ムコ例取ふやうゃ月死スぶ場個トタユメ授脱方イノ打衛ぽ形78録ク対般おなゅッ数書ひごぐ信29抱診2療いゆドが性刻草発刑つ。牛レ学筒すえン般帯され市膚タシ外局1車人セヘナク交後ラ周伊2病煙しんよな高役たごトス。様暮ゃ生未エクタマ落購メト方年めき売簡しまよこ右月スネテ会参ワナカ横浦ょぐんぎ続眠レタヤル峠難レ万始者にぴおく裁応ト境上幻ッさぽよ。"),
        new Note(5, "必きラ払度サヘ文新不イぎさ次呂シ階集サ一入っじぎ村略ほむい王5選そす実向ヌオノ殖住さし報検ち。賞がへルど秘見面泉購英イマ新座ツカホテ代感ケヌ開往フゃ恋写き張府カエ百歌ムソヱ供54撮サアレチ話業ア十気はっ全事ゆゅ向臓漢宇ばやべ。長7営ぞゃ宮知育4松の回速スニ配45体せとお無自ム頭第もスろフ法過ホスアト米南果れすさづ毎者はぎすむ提在つぶ明断モコホ野型栃ゅ。")
    };
}
