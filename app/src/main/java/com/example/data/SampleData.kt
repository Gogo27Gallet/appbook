package com.example.data

import com.example.model.BookChapter
import com.example.model.BookCoverStyle
import com.example.model.BookItem
import com.example.model.CollectionItem
import com.example.model.ReadingStatus

object SampleData {

    // 1. The Power of Discipline (Daniel Walter)
    val powerOfDiscipline = BookItem(
        id = "b_discipline",
        title = "THE POWER OF DISCIPLINE",
        author = "DANIEL WALTER",
        subtitle = "How to Use Self Control and Mental Toughness to Achieve Your Goals",
        coverStyle = BookCoverStyle.POWER_OF_DISCIPLINE,
        coverImageUrl = BookCoverService.COVER_POWER_OF_DISCIPLINE,
        primaryColorHex = 0xFFFFDD00,
        rating = 4.9f,
        pageCount = 208,
        currentPage = 35,
        category = "Self Development",
        quote = "Discipline is choosing between what you want now and what you want most.",
        description = "Discover the psychological strategies to build unbreakable self-discipline, eliminate procrastination, and master daily habits.",
        isTopShelf = true,
        status = ReadingStatus.READING,
        chapters = listOf(
            BookChapter(
                number = 1,
                title = "The Foundation of Self-Discipline",
                paragraphs = listOf(
                    "Self-discipline is not an innate trait bestowed upon a fortunate few; it is a mental muscle that grows stronger with deliberate practice.",
                    "Every day, you are confronted with hundreds of choices between immediate gratification and long-term fulfillment. The undisciplined mind instinctively surrenders to comfort.",
                    "When you learn to detach emotion from execution, doing what needs to be done becomes as natural as breathing. You no longer wait for motivation; you generate momentum."
                )
            ),
            BookChapter(
                number = 2,
                title = "Eliminating the Seeds of Procrastination",
                paragraphs = listOf(
                    "Procrastination is rarely about laziness. In truth, it is an emotional regulation problem. We avoid tasks that trigger anxiety, boredom, or fear of imperfection.",
                    "The antidote is simple: shrink the resistance threshold. Commit to five minutes of focused effort. Once action begins, the brain shifts into completion mode."
                )
            ),
            BookChapter(
                number = 3,
                title = "Designing an Unshakable Routine",
                paragraphs = listOf(
                    "Relying on willpower is a losing battle. The most disciplined individuals structure their physical environment to make good choices effortless and poor choices friction-heavy.",
                    "Remove distractions before they tempt you. Protect your morning hours with absolute reverence."
                )
            )
        )
    )

    // 2. Atomic Habits (James Clear)
    val atomicHabits = BookItem(
        id = "b_habits",
        title = "Atomic Habits",
        author = "James Clear",
        subtitle = "An Easy & Proven Way to Build Good Habits & Break Bad Ones",
        coverStyle = BookCoverStyle.ATOMIC_HABITS,
        coverImageUrl = BookCoverService.COVER_ATOMIC_HABITS,
        primaryColorHex = 0xFFF6F3EB,
        rating = 4.9f,
        pageCount = 320,
        currentPage = 142,
        category = "Mindful",
        quote = "You do not rise to the level of your goals. You fall to the level of your systems.",
        description = "A revolutionary system for getting 1 percent better every day. James Clear distills biology, psychology, and neuroscience into an actionable framework.",
        isTopShelf = true,
        status = ReadingStatus.READING,
        chapters = listOf(
            BookChapter(
                number = 1,
                title = "The Surprising Power of Atomic Habits",
                paragraphs = listOf(
                    "It is so easy to overestimate the importance of one defining moment and underestimate the value of making small improvements on a daily basis.",
                    "Too often, we convince ourselves that massive success requires massive action. Whether it is losing weight, building a business, or writing a book, we put pressure on ourselves to make some earth-shattering improvement.",
                    "Meanwhile, improving by 1 percent isn't particularly notable—sometimes it isn't even noticeable—but it can be far more meaningful, especially in the long run. If you can get 1 percent better each day for one year, you'll end up thirty-seven times better by the time you're done.",
                    "Habits are the compound interest of self-improvement. The same way that money multiplies through compound interest, the effects of your habits multiply as you repeat them."
                )
            ),
            BookChapter(
                number = 2,
                title = "How Your Habits Shape Your Identity",
                paragraphs = listOf(
                    "The ultimate form of intrinsic motivation is when a habit becomes part of your identity. It's one thing to say I'm the type of person who wants this. It's something very different to say I'm the type of person who is this.",
                    "The goal is not to read a book, the goal is to become a reader. The goal is not to run a marathon, the goal is to become a runner. Every action you take is a vote for the type of person you wish to become."
                )
            ),
            BookChapter(
                number = 3,
                title = "The 4 Laws of Behavior Change",
                paragraphs = listOf(
                    "1st Law: Make it obvious. Clear cues spark action.",
                    "2nd Law: Make it attractive. Dopamine drives desire.",
                    "3rd Law: Make it easy. Reduce friction to start.",
                    "4th Law: Make it satisfying. Immediate reward cements repetition."
                )
            )
        )
    )

    // 3. The Book of Five Rings (Miyamoto Musashi)
    val fiveRings = BookItem(
        id = "b_five_rings",
        title = "The Book of Five Rings",
        author = "Miyamoto Musashi",
        subtitle = "Classic Guide to Strategy, Martial Arts, and Mindset",
        coverStyle = BookCoverStyle.BOOK_OF_FIVE_RINGS,
        coverImageUrl = BookCoverService.COVER_FIVE_RINGS,
        primaryColorHex = 0xFF6E1824,
        rating = 4.8f,
        pageCount = 112,
        currentPage = 48,
        category = "Philosophy",
        quote = "There is nothing outside of yourself that can enable you to get better, stronger, richer, or smarter. Everything is within.",
        description = "The immortal manual of strategy written by Japan's greatest samurai swordmaster on achieving total mastery through clarity and relentless dedication.",
        isTopShelf = true,
        status = ReadingStatus.READING,
        chapters = listOf(
            BookChapter(
                number = 1,
                title = "The Ground Book",
                paragraphs = listOf(
                    "Strategy is the craft of the warrior. Commanders must practice it, and soldiers must know the Way. But today in the world, there are no warriors who clearly understand the Way of Strategy.",
                    "First, let us compare the master craftsman and the warrior. The carpenter uses a master plan and builds according to measure. In the same way, the strategist plans with precision and executes without hesitation.",
                    "Do nothing which is of no use. Perceive those things which cannot be seen with the eye. In your spirit, remain tranquil yet alert."
                )
            ),
            BookChapter(
                number = 2,
                title = "The Water Book",
                paragraphs = listOf(
                    "Water adopts the shape of its receptacle, it is sometimes a trickle and sometimes a wild sea. So with strategy, your mind must be like water: fluid, adaptable, and impossible to pin down.",
                    "In battle, if you are agitated, your spirit will be scattered. Maintain an everyday attitude in combat, and a combat attitude in daily life."
                )
            )
        )
    )

    // 4. ONE (Bradley Charbonneau)
    val oneCharbonneau = BookItem(
        id = "b_one",
        title = "ONE",
        author = "Bradley Charbonneau",
        subtitle = "Become Tomorrow's You Today",
        coverStyle = BookCoverStyle.ONE_CHARBONNEAU,
        primaryColorHex = 0xFFF4F4F6,
        rating = 4.7f,
        pageCount = 224,
        currentPage = 60,
        category = "Mindful",
        quote = "Focus on the single seed. Water it relentlessly, and the forest will follow.",
        description = "Distill your scattered ambitions into a single defining focal point and engineer compound momentum toward your true future self.",
        isTopShelf = true,
        status = ReadingStatus.READING,
        chapters = listOf(
            BookChapter(
                number = 1,
                title = "The Power of Single Focus",
                paragraphs = listOf(
                    "When everything is important, nothing is. In a world drowning in digital noise and fractured attention, the rarest superpower is singular focus.",
                    "Pick one needle-moving endeavor. Commit to it without compromise for one hundred consecutive days."
                )
            )
        )
    )

    // Babylon, Orphaned Believers, Next of Kin, Conform, Too Big to Fail, etc.
    val babylonChapters = listOf(
        BookChapter(
            number = 1,
            title = "The Man Who Desired Gold",
            paragraphs = listOf(
                "Bansir, the chariot builder of Babylon, was thoroughly disheartened. From his seat upon the low surrounding wall of his property, he gazed mournfully at his simple home and the open workshop in which stood an unfinished chariot.",
                "His wife frequently appeared at the open door. Her furtive glances in his direction reminded him that the meal sack was almost empty and he ought to be at work finishing the chariot, hammering and carving, polishing and painting.",
                "Kobbi, the musician, his best friend, approached him with a smile. 'Bansir, my generous friend,' he said, 'hast thou not two shekels thou couldst lend me until after the noble's feast tonight?'",
                "'If I had two shekels,' Bansir responded dejectedly, 'to no one would I lend them—not even to thee; for they would be my fortune—my entire fortune. No one lends his entire fortune, not even to his best friend.'",
                "'We are like slaves,' groaned Bansir. 'How many years have we laboured, yet we have naught to show for it! In old Babylon, the richest city in the world, why should we dwell in poverty? Let us go to Arkad, the richest man in all of Babylon, and ask how he attained his fortune.'"
            )
        ),
        BookChapter(
            number = 2,
            title = "The Richest Man in Babylon",
            paragraphs = listOf(
                "In old Babylon there once dwelt a certain rich man named Arkad. Far and wide he was famed for his great wealth. Also was he famed for his liberality. He was generous in his charities, generous with his family, and liberal in his own expenses.",
                "Certain friends of his younger days came to him and said: 'Thou, Arkad, art more fortunate than we. Thou hast become the richest man in all Babylon while we struggle for existence. Yet in our youth, we studied under the same master. Thou wert not more studious nor more honorable.'",
                "Arkad spoke softly: 'If you have not acquired more than a bare existence in the years since we were youths, it is because you either have failed to learn the laws that govern the building of wealth, or else you do not observe them.'",
                "'I found the road to wealth when I decided that a part of all I earned was mine to keep. And so will you. No matter how little you earn, not less than one-tenth should be put aside. Pay yourself first, young man!'"
            )
        ),
        BookChapter(
            number = 3,
            title = "The Five Laws of Gold",
            paragraphs = listOf(
                "Gold cometh gladly and in increasing quantity to any man who will put by not less than one-tenth of his earnings to create an estate for his future and that of his family.",
                "Gold laboreth diligently and contentedly for the wise owner who finds for it profitable employment, multiplying even as the flocks of the field.",
                "Gold clingeth to the protection of the cautious owner who invests it only under the advice of men wise in its handling."
            )
        )
    )

    val orphanedBelievers = BookItem(
        id = "b1",
        title = "ORPHANED BELIEVERS",
        author = "SARA BILLUPS",
        subtitle = "A Journey Through Faith and Belonging",
        coverStyle = BookCoverStyle.ORPHANED_BELIEVERS,
        primaryColorHex = 0xFFC72828,
        rating = 4.8f,
        pageCount = 272,
        currentPage = 42,
        category = "Spiritual",
        quote = "Belonging is not about conformity; it is about standing true in love.",
        description = "A compassionate exploration of spiritual identity, unpacking modern faith and reclaiming purpose in community.",
        isTopShelf = true,
        status = ReadingStatus.READING,
        chapters = listOf(
            BookChapter(
                number = 1,
                title = "A Table in the Wilderness",
                paragraphs = listOf(
                    "We were raised on certainty and fluorescent sanctuaries, told that faith was a blueprint without cracks. But life has an uncanny habit of shaking foundations until only what is real remains.",
                    "I remember the quiet dusk of the Pacific Northwest, rain softly striking the tall pines. For months I had felt like an orphan of my childhood tradition, disconnected from easy answers yet hungry for authentic sacred space.",
                    "In ancient spiritual lineages, exile is never an accident. It is the fertile soil where true seeking begins. When the structures fall away, we finally see the open sky above us."
                )
            ),
            BookChapter(
                number = 2,
                title = "Reclaiming the Sacred",
                paragraphs = listOf(
                    "To belong does not mean to surrender your discernment. It means discovering that the divine table is infinitely wider than the narrow doors we were taught to guard.",
                    "Here, in the gathering of honest questions, we find a peace that does not require uniformity."
                )
            )
        )
    )

    val nextOfKin = BookItem(
        id = "b2",
        title = "NEXT OF KIN",
        author = "GABRIELLE HAMILTON",
        subtitle = "Roots and Resonance",
        coverStyle = BookCoverStyle.NEXT_OF_KIN,
        primaryColorHex = 0xFFD85324,
        rating = 4.9f,
        pageCount = 310,
        currentPage = 115,
        category = "Spiritual",
        quote = "The branches seek the sun, but the deep soil knows the story.",
        description = "A stirring memoir of family lineage, personal resilience, and discovering one's roots across generations.",
        isTopShelf = true,
        status = ReadingStatus.READING,
        chapters = listOf(
            BookChapter(
                number = 1,
                title = "The Soil and the Seed",
                paragraphs = listOf(
                    "My grandmother used to say that people are like river stones, polished not by comfort, but by the relentless current of time and memory.",
                    "In our family kitchen, stories were exchanged like currency. The oak table held generations of flour, spilled tea, and deep laughter.",
                    "To understand who you are, you must kneel and touch the earth where your ancestors planted their hopes."
                )
            )
        )
    )

    val conform = BookItem(
        id = "b3",
        title = "CONFORM",
        author = "ARIEL SULLIVAN",
        subtitle = "The Architecture of Thought",
        coverStyle = BookCoverStyle.CONFORM,
        primaryColorHex = 0xFFF2ECE1,
        rating = 4.7f,
        pageCount = 340,
        currentPage = 80,
        category = "Spiritual",
        quote = "Break the mould not in anger, but in quiet clarity of purpose.",
        description = "A philosophical study on the tension between societal expectations and authentic spiritual freedom.",
        isTopShelf = true,
        status = ReadingStatus.READING,
        chapters = listOf(
            BookChapter(
                number = 1,
                title = "The Architecture of Expectation",
                paragraphs = listOf(
                    "From the earliest age, society surrounds us with invisible molds. We are rewarded for matching the pattern and questioned when we deviate.",
                    "True spiritual clarity is not rebellion for the sake of friction. It is the quiet courage to align with inner truth even when the world applauds the counterfeit."
                )
            )
        )
    )

    val richestMan = BookItem(
        id = "b4",
        title = "THE RICHEST MAN IN BABYLON",
        author = "GEORGE S. CLASON",
        subtitle = "Timeless financial parables",
        coverStyle = BookCoverStyle.RICHEST_MAN_BABYLON,
        coverImageUrl = BookCoverService.COVER_RICHEST_MAN,
        primaryColorHex = 0xFF1C2C48,
        rating = 4.9f,
        pageCount = 194,
        currentPage = 24,
        category = "Finances",
        quote = "A part of all you earn is yours to keep. Gold clingeth to the protection of the cautious owner.",
        description = "Ancient Babylonian parables that lay down the core foundations of wealth creation, thrift, and financial wisdom.",
        isTopShelf = false,
        status = ReadingStatus.READING,
        chapters = babylonChapters
    )

    val tooBigToFail = BookItem(
        id = "b5",
        title = "TOO BIG TO FAIL",
        author = "ANDREW ROSS SORKIN",
        subtitle = "How Wall Street and Washington Fought to Save the System",
        coverStyle = BookCoverStyle.TOO_BIG_TO_FAIL,
        coverImageUrl = BookCoverService.COVER_TOO_BIG_TO_FAIL,
        primaryColorHex = 0xFFD92828,
        rating = 4.8f,
        pageCount = 640,
        currentPage = 152,
        category = "Finances",
        quote = "Confidence in the financial system is like oxygen: when you have it, you take it for granted; when you don't, it's all you think about.",
        description = "The definitive inside account of the greatest financial crisis since the Great Depression, told moment by moment.",
        isTopShelf = false,
        status = ReadingStatus.READING,
        chapters = listOf(
            BookChapter(
                number = 1,
                title = "The Weekend at Lehman",
                paragraphs = listOf(
                    "On Friday evening, September 12, 2008, the titans of Wall Street gathered inside the Federal Reserve Bank of New York at 33 Liberty Street.",
                    "Dick Fuld sat in his office fifty blocks uptown, watching thirty years of empire evaporate on glowing terminal screens. No government bailout was coming.",
                    "Treasury Secretary Hank Paulson looked across the conference table at the gathered chief executives and made it clear: find a private solution, or brace for economic impact."
                )
            )
        )
    )

    val powerOfNow = BookItem(
        id = "b6",
        title = "THE POWER OF NOW",
        author = "ECKHART TOLLE",
        subtitle = "A Guide to Spiritual Enlightenment",
        coverStyle = BookCoverStyle.POWER_OF_NOW,
        coverImageUrl = BookCoverService.COVER_POWER_OF_NOW,
        primaryColorHex = 0xFFD4A034,
        rating = 4.8f,
        pageCount = 236,
        currentPage = 236,
        category = "Spiritual",
        quote = "Realize deeply that the present moment is all you will ever have.",
        description = "An indispensable spiritual manual teaching us to live in the eternal present beyond ego and mental anxiety.",
        isTopShelf = false,
        status = ReadingStatus.COMPLETED
    )

    val mansSearch = BookItem(
        id = "b7",
        title = "MAN'S SEARCH FOR MEANING",
        author = "VIKTOR E. FRANKL",
        subtitle = "Classic Tribute to Hope from the Holocaust",
        coverStyle = BookCoverStyle.MANS_SEARCH,
        coverImageUrl = BookCoverService.COVER_MANS_SEARCH,
        primaryColorHex = 0xFF2E5339,
        rating = 4.9f,
        pageCount = 200,
        currentPage = 160,
        category = "Spiritual",
        quote = "When we are no longer able to change a situation, we are challenged to change ourselves.",
        description = "A transformative masterpiece describing Frankl's psychotherapeutic method of logotherapy and finding purpose in any circumstance.",
        isTopShelf = false,
        status = ReadingStatus.READING
    )

    val trust = BookItem(
        id = "b8",
        title = "TRUST",
        author = "HERNAN DIAZ",
        subtitle = "Winner of the Pulitzer Prize",
        coverStyle = BookCoverStyle.TRUST,
        coverImageUrl = BookCoverService.COVER_TRUST,
        primaryColorHex = 0xFF1C6347,
        rating = 4.7f,
        pageCount = 416,
        currentPage = 280,
        category = "Finances",
        quote = "Every financial fortune is a narrative built upon belief and collective agreement.",
        description = "An exhilarating novel that sets out to deceive and reveal the mysteries of American capitalism.",
        isTopShelf = true,
        status = ReadingStatus.READING
    )

    val electricCity = BookItem(
        id = "b9",
        title = "ELECTRIC CITY",
        author = "THOMAS HAGER",
        subtitle = "The Lost History of Ford and Edison's American Utopia",
        coverStyle = BookCoverStyle.ELECTRIC_CITY,
        primaryColorHex = 0xFF21252B,
        rating = 4.6f,
        pageCount = 304,
        currentPage = 120,
        category = "Finances",
        quote = "Power flows where visionary ambition converges with technical determination.",
        description = "The dramatic story of Henry Ford and Thomas Edison's audacious attempt to reinvent American industry.",
        isTopShelf = true,
        status = ReadingStatus.WANT_TO_READ
    )

    val practicalUncertainty = BookItem(
        id = "b10",
        title = "PRACTICAL UNCERTAINTY",
        author = "E. D. MILLER",
        subtitle = "Decision Making Under Chaos",
        coverStyle = BookCoverStyle.PRACTICAL_UNCERTAINTY,
        primaryColorHex = 0xFFFDFDFD,
        rating = 4.7f,
        pageCount = 260,
        currentPage = 95,
        category = "Finances",
        quote = "To master risk is not to eradicate doubt, but to calibrate action.",
        description = "A strategic playbook for navigating modern market fluctuations with mental models and risk discipline.",
        isTopShelf = true,
        status = ReadingStatus.READING
    )

    val nervousSystem = BookItem(
        id = "b11",
        title = "NERVOUS SYSTEM",
        author = "DR. LINNEA MOORE",
        subtitle = "Restoring Mind-Body Balance",
        coverStyle = BookCoverStyle.NERVOUS_SYSTEM,
        primaryColorHex = 0xFFD8D232,
        rating = 4.8f,
        pageCount = 320,
        currentPage = 175,
        category = "Mindful",
        quote = "Calm is not the absence of energy, but the symphony of regulated breath.",
        description = "Cutting-edge neuroscience and somatic exercises to heal the vagus nerve and dissolve chronic tension.",
        isTopShelf = true,
        status = ReadingStatus.READING
    )

    val theMagic = BookItem(
        id = "b12",
        title = "THE MAGIC OF THINKING BIG",
        author = "DAVID J. SCHWARTZ",
        subtitle = "Achieve the Results You Always Wanted",
        coverStyle = BookCoverStyle.THE_MAGIC,
        primaryColorHex = 0xFF147B88,
        rating = 4.9f,
        pageCount = 336,
        currentPage = 336,
        category = "Mindful",
        quote = "Believe it can be done. When you believe something can be done, really believe, your mind will find ways to do it.",
        description = "Millions of people throughout the world have improved their lives using this timeless classic.",
        isTopShelf = true,
        status = ReadingStatus.COMPLETED
    )

    // Library top selections from Screenshot_20261011_163353_X.jpg
    val topSelectionsForYou = listOf(
        powerOfDiscipline,
        atomicHabits,
        fiveRings,
        oneCharbonneau,
        richestMan,
        tooBigToFail
    )

    val initialCollections = listOf(
        CollectionItem(
            id = "c1",
            title = "Spiritual",
            subtitle = "Manifest your inner self",
            lastUpdatedText = "2 Hours ago",
            bookCount = 93,
            featuredBooks = listOf(orphanedBelievers, nextOfKin, conform, richestMan, tooBigToFail, powerOfNow),
            allBooks = listOf(
                orphanedBelievers,
                nextOfKin,
                conform,
                richestMan,
                tooBigToFail,
                powerOfNow,
                mansSearch
            )
        ),
        CollectionItem(
            id = "c2",
            title = "Finances",
            subtitle = "Grow and manage your finances.",
            lastUpdatedText = "3 Days ago",
            bookCount = 48,
            featuredBooks = listOf(trust, electricCity, practicalUncertainty, richestMan, tooBigToFail),
            allBooks = listOf(
                trust,
                electricCity,
                practicalUncertainty,
                richestMan,
                tooBigToFail
            )
        ),
        CollectionItem(
            id = "c3",
            title = "Mindful",
            subtitle = "Expand your mind",
            lastUpdatedText = "12 Days ago",
            bookCount = 54,
            featuredBooks = listOf(nervousSystem, theMagic, powerOfNow, mansSearch, conform),
            allBooks = listOf(
                nervousSystem,
                theMagic,
                powerOfNow,
                mansSearch,
                conform
            )
        )
    )
}
