// ── Config (REPLACED FOR FREE GOOGLE GEMINI) ────────────────
const API_KEY = process.env.GEMINI_API_KEY;
const API_URL = `https://generativelanguage.googleapis.com/v1beta/models/gemini-3.1-flash-lite-preview:generateContent?key=${API_KEY}`;
// ── System Prompt ──────────────────────────────────────────
const SYSTEM_PROMPT = `You are Nyay AI, a knowledgeable and empathetic Indian legal rights advisor. 
Your mission is to help ordinary people understand what Indian law says and how to protect themselves.

When someone describes a situation:
1. Clearly state their RIGHTS in plain, simple language.
2. Cite the SPECIFIC Indian laws, acts, or sections (e.g., "Section 106 of the Transfer of Property Act 1882").
3. Give PRACTICAL STEPS they can take, numbered clearly.
4. Mention the AUTHORITY or FORUM they can approach.
5. Mention any TIME LIMITS for filing complaints.

IMPORTANT:
- If a person needs urgent help (physical threat), tell them to call 112 immediately.
- Always end with: a reminder that this is general information and they should consult a lawyer.

Format your response using these headers:
**Your Rights**
**Applicable Laws**
**Steps You Can Take**
**Where to Complain**`;

// ── State ──────────────────────────────────────────────────
let conversationHistory = [];

// ── DOM refs ───────────────────────────────────────────────
const chatBox   = document.getElementById("chat-box");
const userInput = document.getElementById("user-input");
const sendBtn   = document.getElementById("send-btn");

// ── Format AI response ─────────────────────────────────────
function formatResponse(text) {
  // Bold section headers
  text = text.replace(/\*\*(.*?)\*\*/g, '<span class="section-head">$1</span>');
  // Numbered lists
  text = text.replace(/\n(\d+)\.\s/g, '<br><strong>$1.</strong> ');
  // Line breaks
  text = text.replace(/\n/g, '<br>');
  return text;
}

// ── Add message to chat ─────────────────────────────────────
function addMessage(role, text, isLoading = false) {
  const msgDiv = document.createElement("div");
  msgDiv.className = "msg " + (role === "user" ? "user" : "ai");

  const avatar = document.createElement("div");
  avatar.className = "avatar " + (role === "user" ? "user" : "ai");
  avatar.textContent = role === "user" ? "You" : "⚖";

  const bubble = document.createElement("div");
  bubble.className = "bubble" + (isLoading ? " loading" : "");
  bubble.innerHTML = isLoading ? text : formatResponse(text);

  msgDiv.appendChild(avatar);
  msgDiv.appendChild(bubble);
  chatBox.appendChild(msgDiv);
  chatBox.scrollTop = chatBox.scrollHeight;

  return bubble;
}

// ── Send message ────────────────────────────────────────────
async function sendMsg() {
  const text = userInput.value.trim();
  if (!text || sendBtn.disabled) return;

  userInput.value = "";
  sendBtn.disabled = true;
  autoResize();

  // Add user message to UI
  addMessage("user", text);
  
  // Update History for Gemini
  conversationHistory.push({ role: "user", parts: [{ text: text }] });

  // Add loading indicator
  const loadingBubble = addMessage("ai", "Analysing your situation under Indian law...", true);

  try {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        contents: [
          { role: "user", parts: [{ text: SYSTEM_PROMPT }] }, // Set the "Persona"
          ...conversationHistory
        ]
      })
    });

    if (!response.ok) {
      const err = await response.json();
      throw new Error(err?.error?.message || "API error");
    }

    const data = await response.json();
    const reply = data.candidates[0].content.parts[0].text;

    // Save AI response to history
    conversationHistory.push({ role: "model", parts: [{ text: reply }] });

    // Update UI
    loadingBubble.className = "bubble";
    loadingBubble.innerHTML = formatResponse(reply);
    chatBox.scrollTop = chatBox.scrollHeight;

  } catch (error) {
    loadingBubble.className = "bubble";
    loadingBubble.innerHTML =
      `<span style="color:#c0392b">Error: ${error.message}</span><br><br>
      Please check your API key or internet connection.`;
    console.error("API Error:", error);
  }

  sendBtn.disabled = false;
  userInput.focus();
}

// ── Quick topic shortcut ────────────────────────────────────
function ask(question) {
  userInput.value = question;
  sendMsg();
}

// ── Auto-resize textarea ────────────────────────────────────
function autoResize() {
  userInput.style.height = "auto";
  userInput.style.height = Math.min(userInput.scrollHeight, 120) + "px";
}

userInput.addEventListener("input", autoResize);

userInput.addEventListener("keydown", (e) => {
  if (e.key === "Enter" && !e.shiftKey) {
    e.preventDefault();
    sendMsg();
  }
});