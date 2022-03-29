public class GuitarHero {
    private static final double CONCERT = 440.0;
    private static final String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

    public static void main(String[] args) {
        /* create guitar strings for concerts of keyboard. */
        int length = keyboard.length();
        synthesizer.GuitarString[] stringForConcert = new synthesizer.GuitarString[length];
        for (int i = 0; i < length; i++) {
            double frequency = CONCERT * Math.pow(2, (i - 24.0) / 12.0);
            stringForConcert[i] = new synthesizer.GuitarString(frequency);
        }

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = keyboard.indexOf(key);
                if (index != -1) {
                    stringForConcert[index].pluck();
                }
            }

            /* compute the superposition of samples */
            double sample = 0.0;
            for (int i = 0; i < length; i++) {
                sample += stringForConcert[i].sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (int i = 0; i < length; i++) {
                stringForConcert[i].tic();
            }
        }
    }
}
