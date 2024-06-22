from fastapi import FastAPI, File, UploadFile, HTTPException
app = FastAPI()
import os
import whisper
# base - os언어, base.en 영어 
#자세한 내용은 링크 참조 https://github.com/openai/whisper
model = whisper.load_model("base.en")


@app.get("/")
async def root():
    return {"message":"Hello world!1"}

@app.post("/audio/transcriptions2")
async def audio_transcriptions(file: UploadFile = File(...)):
    # 임시 파일에 업로드된 파일 저장
    print("accpeted.")
    file_location = f"temp_{file.filename}"
    with open(file_location, "wb") as f:
        f.write(await file.read())

    result = model.transcribe(file_location)

    # 임시 파일 삭제
    os.remove(file_location)
    print(result)
    return result["text"]


if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="127.0.0.1", port=8000, reload=True)