from transformers import AutoTokenizer, AutoModelForTokenClassification
from transformers import pipeline
import sys

def apply_ner(input_text):
    tokenizer = AutoTokenizer.from_pretrained("dslim/bert-base-NER")
    model = AutoModelForTokenClassification.from_pretrained("dslim/bert-base-NER")
    nlp = pipeline("ner", model=model, tokenizer=tokenizer)
    return nlp(input_text)

text = sys.stdin.read()
ner_output = apply_ner(text)
print(ner_output)