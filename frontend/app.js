// Базовый URL бэкенда (если порт другой — поправь)
const API_BASE = "http://localhost:8080/api/dishes";

let allDishes = [];          // все блюда
let selectedType = "ALL";    // текущий фильтр по типу
let currentDish = null;      // выбранное блюдо (детали)
let currentDishBase = null;  // базовые калории/цена без соуса

// корзина: элементы вида { key, dishId, name, caloriesPerUnit, costPerUnit, count }
let cartItems = [];

// ================== ИНИЦИАЛИЗАЦИЯ ==================

document.addEventListener("DOMContentLoaded", () => {
  initEventListeners();
  loadDishes();
});

function initEventListeners() {
  // Поиск
  const searchInput = document.getElementById("search-input");
  if (searchInput) {
    searchInput.addEventListener("input", applyFilters);
  }

  // Табы по типам
  const tabsContainer = document.getElementById("type-tabs");
  if (tabsContainer) {
    tabsContainer.addEventListener("click", (e) => {
      const btn = e.target.closest(".type-tab");
      if (!btn) return;

      document.querySelectorAll(".type-tab")
        .forEach(b => b.classList.remove("active"));
      btn.classList.add("active");

      selectedType = btn.dataset.type || "ALL";
      applyFilters();
    });
  }

  // Очистка корзины
  const clearBtn = document.getElementById("clear-cart-btn");
  if (clearBtn) {
    clearBtn.addEventListener("click", () => {
      cartItems = [];
      renderCart();
    });
  }
}

// ================== ЗАГРУЗКА СПИСКА БЛЮД ==================

async function loadDishes() {
  const messageEl = document.getElementById("list-message");
  try {
    if (messageEl) {
      messageEl.textContent = "Загрузка меню...";
    }

    const response = await fetch(API_BASE);
    if (!response.ok) {
      throw new Error("Ошибка при загрузке списка блюд");
    }
    const data = await response.json();
    allDishes = data;

    if (messageEl) {
      messageEl.textContent = "";
    }

    applyFilters();
  } catch (error) {
    console.error(error);
    if (messageEl) {
      messageEl.textContent = "Не удалось загрузить меню. Попробуй обновить страницу позже.";
    }
  }
}

// ================== ФИЛЬТРАЦИЯ ==================

function applyFilters() {
  const searchInput = document.getElementById("search-input");
  const searchText = (searchInput?.value || "").toLowerCase().trim();

  let filtered = allDishes.slice();

  // Поиск по названию
  if (searchText.length > 0) {
    filtered = filtered.filter(dish =>
      dish.name.toLowerCase().includes(searchText)
    );
  }

  // Фильтр по типу
  if (selectedType !== "ALL") {
    filtered = filtered.filter(dish => dish.type === selectedType);
  }

  renderDishTable(filtered);
}

// ================== ТАБЛИЦА БЛЮД ==================

function renderDishTable(dishes) {
  const table = document.getElementById("dishes-table");
  if (!table) {
    console.error("Таблица #dishes-table не найдена");
    return;
  }

  const tbody = table.querySelector("tbody");
  if (!tbody) {
    console.error("tbody у таблицы #dishes-table не найден");
    return;
  }

  tbody.innerHTML = "";

  if (!dishes || dishes.length === 0) {
    const tr = document.createElement("tr");
    const td = document.createElement("td");
    td.colSpan = 6;
    td.textContent = "Блюда не найдены";
    tr.appendChild(td);
    tbody.appendChild(tr);
    return;
  }

  dishes.forEach(dish => {
    const tr = document.createElement("tr");

    // клик по строке — показать детали
    tr.addEventListener("click", () => loadDishDetails(dish.id));

    const idTd = document.createElement("td");
    idTd.textContent = dish.id;

    const nameTd = document.createElement("td");
    nameTd.textContent = dish.name;

    const typeTd = document.createElement("td");
    typeTd.textContent = dish.type;

    const kcalTd = document.createElement("td");
    kcalTd.textContent = dish.totalCalories.toFixed(0);

    const costTd = document.createElement("td");
    costTd.textContent = dish.totalCost.toFixed(2);

    const actionTd = document.createElement("td");
    const addBtn = document.createElement("button");
    addBtn.textContent = "+";
    addBtn.className = "add-to-cart-btn";
    addBtn.title = "Добавить в заказ";

    // важно: не триггерить клик по строке (детали)
    addBtn.addEventListener("click", (event) => {
      event.stopPropagation();
      // добавляем базовое блюдо БЕЗ соуса
      addDishToCart(dish.id, dish.totalCalories, dish.totalCost, "");
    });

    actionTd.appendChild(addBtn);

    tr.appendChild(idTd);
    tr.appendChild(nameTd);
    tr.appendChild(typeTd);
    tr.appendChild(kcalTd);
    tr.appendChild(costTd);
    tr.appendChild(actionTd);

    tbody.appendChild(tr);
  });
}

// ================== ДЕТАЛИ БЛЮДА ==================

async function loadDishDetails(id) {
  const detailsEl = document.getElementById("dish-details");
  try {
    const response = await fetch(`${API_BASE}/${id}`);
    if (!response.ok) {
      throw new Error("Ошибка при загрузке деталей блюда");
    }
    const dish = await response.json();
    currentDish = dish;
    renderDishDetails(dish);
  } catch (error) {
    console.error(error);
    if (detailsEl) {
      detailsEl.innerHTML = `<p class="error">Не удалось загрузить детали блюда.</p>`;
    }
  }
}

function renderDishDetails(dish) {
  const container = document.getElementById("dish-details");
  if (!container) {
    console.error("Не найден блок #dish-details");
    return;
  }

  if (!dish) {
    container.innerHTML = `
      <div class="placeholder">
        <h3>Блюдо не выбрано</h3>
        <p>Нажми на любое блюдо слева, чтобы посмотреть его состав.</p>
      </div>
    `;
    return;
  }

  currentDishBase = {
    calories: dish.totalCalories,
    cost: dish.totalCost
  };

  const isDessert = dish.type === "DESSERT";

  // Поддержка разных имён полей в JSON
  const rawIngredients =
    dish.ingredients ||
    dish.ingredientDetails ||
    dish.components ||
    dish.items ||
    [];

  const ingredientsRows = rawIngredients
    .map(ing => {
      const name =
        ing.name ??
        ing.ingredientName ??
        ing.title ??
        "Ингредиент";

      const grams =
        ing.grams ??
        ing.amount ??
        ing.weight ??
        0;

      const calories =
        ing.calories ??
        ing.kcal ??
        0;

      const cost =
        ing.cost ??
        ing.price ??
        0;

      return `
        <tr>
          <td>${name}</td>
          <td>${grams}</td>
          <td>${Number(calories).toFixed(0)}</td>
          <td>${Number(cost).toFixed(2)}</td>
        </tr>
      `;
    })
    .join("");

  const spicyHtml = isDessert
    ? `<p class="spicy-disabled">Добавление острого соуса не применяется к десертам.</p>`
    : `
      <label class="spicy-option">
        <input type="checkbox" id="spicy-option">
        Добавить острый соус (+50 ккал, +200 тг)
      </label>
    `;

  container.innerHTML = `
    <div class="details-header">
      <h2>${dish.name}</h2>
      <p>Тип: <strong>${dish.type}</strong></p>
    </div>

    <div class="details-summary">
      <p>
        Общая калорийность:
        <strong id="dish-total-cal">${dish.totalCalories.toFixed(0)} ккал</strong>
      </p>
      <p>
        Общая стоимость:
        <strong id="dish-total-cost">${dish.totalCost.toFixed(2)} тг</strong>
      </p>

      ${spicyHtml}

      <button id="add-current-to-cart" type="button" class="add-current-btn">
        Добавить это блюдо в заказ
      </button>
    </div>

    <h3>Состав блюда</h3>
    <table class="ingredients-table">
      <thead>
        <tr>
          <th>Ингредиент</th>
          <th>Масса, г</th>
          <th>Калории</th>
          <th>Стоимость, тг</th>
        </tr>
      </thead>
      <tbody>
        ${ingredientsRows}
      </tbody>
    </table>
  `;

  const spicyCheckbox = document.getElementById("spicy-option");
  if (spicyCheckbox && !isDessert) {
    spicyCheckbox.addEventListener("change", handleSpicyToggle);
  }

  const addCurrentBtn = document.getElementById("add-current-to-cart");
  if (addCurrentBtn) {
    addCurrentBtn.addEventListener("click", addCurrentDishToCart);
  }
}

function handleSpicyToggle(event) {
  if (!currentDishBase || !currentDish) return;
  if (currentDish.type === "DESSERT") return; // на всякий случай

  const add = event.target.checked;
  const calEl = document.getElementById("dish-total-cal");
  const costEl = document.getElementById("dish-total-cost");
  if (!calEl || !costEl) return;

  const baseCals = currentDishBase.calories;
  const baseCost = currentDishBase.cost;

  const newCals = baseCals + (add ? 50 : 0);
  const newCost = baseCost + (add ? 200 : 0);

  calEl.textContent = `${newCals.toFixed(0)} ккал`;
  costEl.textContent = `${newCost.toFixed(2)} тг`;
}

function addCurrentDishToCart() {
  if (!currentDish || !currentDishBase) return;

  let caloriesPerUnit = currentDishBase.calories;
  let costPerUnit = currentDishBase.cost;
  let suffix = "";

  const spicyCheckbox = document.getElementById("spicy-option");
  const canBeSpicy = currentDish.type !== "DESSERT";

  if (spicyCheckbox && canBeSpicy && spicyCheckbox.checked) {
    caloriesPerUnit += 50;
    costPerUnit += 200;
    suffix = " (острый соус)";
  }

  addDishToCart(currentDish.id, caloriesPerUnit, costPerUnit, suffix);
}

// ================== КОРЗИНА ==================

function addDishToCart(dishId, caloriesPerUnit, costPerUnit, labelSuffix) {
  // ищем блюдо в общем списке или берём currentDish
  const baseDish = allDishes.find(d => d.id === dishId) || currentDish;
  if (!baseDish) {
    console.warn("Блюдо с id", dishId, "не найдено");
    return;
  }

  const baseName = baseDish.name;
  const itemName = labelSuffix ? `${baseName}${labelSuffix}` : baseName;
  const variantKey = labelSuffix && labelSuffix.length > 0 ? labelSuffix : "plain";

  const key = `${dishId}_${variantKey}`;

  let item = cartItems.find(x => x.key === key);
  if (item) {
    item.count += 1;
  } else {
    cartItems.push({
      key,
      dishId,
      name: itemName,
      caloriesPerUnit,
      costPerUnit,
      count: 1
    });
  }

  renderCart();
}

function renderCart() {
  const summaryEl = document.getElementById("cart-summary");
  const listEl = document.getElementById("cart-items");
  if (!summaryEl || !listEl) return;

  listEl.innerHTML = "";

  if (cartItems.length === 0) {
    summaryEl.textContent = "Заказ пуст";
    return;
  }

  let totalCount = 0;
  let totalCalories = 0;
  let totalCost = 0;

  cartItems.forEach(item => {
    totalCount += item.count;
    totalCalories += item.caloriesPerUnit * item.count;
    totalCost += item.costPerUnit * item.count;

    const li = document.createElement("li");
    li.textContent =
      `${item.name} × ${item.count} — ` +
      `${(item.caloriesPerUnit * item.count).toFixed(0)} ккал, ` +
      `${(item.costPerUnit * item.count).toFixed(2)} тг`;
    listEl.appendChild(li);
  });

  summaryEl.textContent =
    `Позиций: ${totalCount}, всего: ${totalCalories.toFixed(0)} ккал, ${totalCost.toFixed(2)} тг`;
}
