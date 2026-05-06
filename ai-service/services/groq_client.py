import os
from groq import Groq

client = Groq(api_key=os.getenv("GROQ_API_KEY"))

def call_groq(prompt):
    try:
        response = client.chat.completions.create(
            model="llama-3.3-70b-versatile",
            messages=[
                {"role": "system", "content": "You are a compliance assistant."},
                {"role": "user", "content": prompt}
            ],
            temperature=0.4
        )

        return {
            "success": True,
            "data": response.choices[0].message.content
        }

    except Exception as e:
        print(f"Groq API Error: {e}")
        return {
            "success": False,
            "error": str(e),
            "data": "AI temporarily unavailable. Please refer to manual guidelines.",
            "is_fallback": True
        }