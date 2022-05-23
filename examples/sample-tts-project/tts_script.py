import pyttsx3
engine = pyttsx3.init()
engine.setProperty("rate", 140) # words per minute, 200 default
engine.setProperty("volume", 0.7) # Floating point volume in the range of 0.0 to 1.0 inclusive.
"""VOICE"""
voices = engine.getProperty('voices')       # getting details of current voice
#engine.setProperty('voice', voices[0].id)  # changing index, changes voices. 0 for male
engine.setProperty('voice', voices[1].id)   # changing index, changes voices. 1 for female
engine.say("""Tragulus is a genus of even-toed ungulates in the family Tragulidae that are known as mouse-deer.
In Ancient Greek τράγος (tragos) means a male goat, while the Latin diminutive –ulus means 'tiny'.
With a weight of 0.7–8.0 kg (1.5–17.6 lb) and a length of 40–75 cm (16–30 in),
they are the smallest ungulates in the world, though the largest species of mouse-deer surpass some species of Neotragus antelopes in size.
The mouse-deer are restricted to Southeast Asia from far southern China (south Yunnan) to the Philippines (Balabac) and Java.
""")
engine.runAndWait()