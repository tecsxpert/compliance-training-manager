import chromadb

client = chromadb.Client()
collection = client.get_or_create_collection("docs")

def add_doc(text):
    collection.add(documents=[text], ids=[str(hash(text))])

def query_doc(query):
    res = collection.query(query_texts=[query], n_results=2)
    return res.get("documents", [])